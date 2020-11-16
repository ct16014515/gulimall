package com.iflytek.gulimall.cart.service;

import com.alibaba.fastjson.JSON;
import com.iflytek.gulimall.common.constant.CartConstant;

import com.iflytek.gulimall.common.feign.ProductServiceAPI;
import com.iflytek.gulimall.common.feign.vo.CartItemVO;
import com.iflytek.gulimall.common.feign.vo.SkuInfoVO;
import com.iflytek.gulimall.common.utils.ResultBody;

import com.iflytek.gulimall.cart.interceptor.CartInterceptor;
import com.iflytek.gulimall.cart.to.UserInfoTO;
import com.iflytek.gulimall.cart.vo.CartVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TaskExecutor executor;


    @Autowired
    private ProductServiceAPI productServiceAPI;




    /**
     * 将用户的临时购物车和登录时候的购物车分开放入缓存
     * 存入缓存以map格式 大key为cartKey 小key为skuId.
     *
     * @param skuId
     * @param num
     * @return
     */
    public CartItemVO addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        String cartKey = getString();
        Object o = redisTemplate.opsForHash().get(cartKey, skuId.toString());
        if (o == null) {
            //缓存没有购物车,创建购物车
            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setSkuId(skuId);
            CompletableFuture<Void> SkuInfoVOFuture = CompletableFuture.runAsync(() -> {
                //获取sku属性
                ResultBody<SkuInfoVO> info = productServiceAPI.info(skuId);
                SkuInfoVO skuInfoVO = info.getData();
                cartItemVO.setSkuCount(num);
                cartItemVO.setSkuImg(skuInfoVO.getSkuDefaultImg());
                cartItemVO.setSkuPrice(skuInfoVO.getPrice());
                cartItemVO.setSkuTitle(skuInfoVO.getSkuTitle());
                cartItemVO.setSkuTotalMoney(skuInfoVO.getPrice().multiply(new BigDecimal(num)));
                cartItemVO.setWeight(skuInfoVO.getWeight());
            }, executor);
            CompletableFuture<Void> skuAttrsFuture = CompletableFuture.runAsync(() -> {
                ResultBody<List<String>> resultBody = productServiceAPI.getskuAttrsBySkuId(skuId);
                List<String> skuAttrs = resultBody.getData();
                cartItemVO.setSkuAttrs(skuAttrs);
            }, executor);
            CompletableFuture.allOf(SkuInfoVOFuture, skuAttrsFuture).get();
            String cartItemVOStr = JSON.toJSONString(cartItemVO);
            redisTemplate.opsForHash().put(cartKey, skuId.toString(), cartItemVOStr);
            return cartItemVO;
        } else {
            //有购物车,则数量修改其他不变
            String cartItemVOStr = (String) o;
            CartItemVO cartItemVO = JSON.parseObject(cartItemVOStr, CartItemVO.class);
            cartItemVO.setSkuCount(cartItemVO.getSkuCount() + num);
            cartItemVO.setSkuTotalMoney(cartItemVO.getSkuPrice().multiply(new BigDecimal(cartItemVO.getSkuCount())));
            String jsonString = JSON.toJSONString(cartItemVO);
            redisTemplate.opsForHash().put(cartKey, skuId.toString(), jsonString);
            return cartItemVO;
        }
    }

    /**
     * 从redis获取用户购物车
     * 1、用户没有登录获取临时购物车,
     * 2、用户登录后获取登录后的购物车
     * 3、用户在添加成功页面（success.html）登录后，临时添加的购物车数据需要添加为用户真实的购物车数据,并将临时购物车数据清除
     *
     * @param skuId
     * @return
     */
    public CartItemVO getCarItemVOFromRedis(Long skuId) {
        UserInfoTO userInfoTO = CartInterceptor.toThreadLocal.get();
        Object o = redisTemplate.opsForHash().get(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserKey(), skuId.toString());
        String cartItemVOStr = (String) o;
        CartItemVO cartItemVO = JSON.parseObject(cartItemVOStr, CartItemVO.class);
        if (StringUtils.isEmpty(userInfoTO.getUserId())) {
            return cartItemVO;
        } else {
            return mergeCart(skuId, cartItemVO);
        }
    }

    /**
     * @param skuId      商品id
     * @param cartItemVO 临时购物车数据
     * @return 合并购物车 临时购物车数据合并到长期购物车里面
     */
    private CartItemVO mergeCart(Long skuId, CartItemVO cartItemVO) {
        UserInfoTO userInfoTO = CartInterceptor.toThreadLocal.get();
        Object longTermO = redisTemplate.opsForHash().get(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserId(), skuId.toString());
        String longTermCartItemVOStr = (String) longTermO;
        CartItemVO longTermCartItemVO = JSON.parseObject(longTermCartItemVOStr, CartItemVO.class);
        if (longTermCartItemVO == null) {
            deleteTempCart(skuId, userInfoTO);
            redisTemplate.opsForHash().put(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserId(), skuId.toString(), JSON.toJSONString(cartItemVO));
            return cartItemVO;
        } else {
            if (cartItemVO != null) {
                longTermCartItemVO.setSkuCount(longTermCartItemVO.getSkuCount() + cartItemVO.getSkuCount());
                longTermCartItemVO.setSkuTotalMoney(longTermCartItemVO.getSkuTotalMoney().add(cartItemVO.getSkuTotalMoney()));
                deleteTempCart(skuId, userInfoTO);
                redisTemplate.opsForHash().put(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserId(), skuId.toString(), JSON.toJSONString(longTermCartItemVO));
            }
            return longTermCartItemVO;
        }
    }

    /**
     * 清除临时购物车
     *
     * @param skuId
     * @param userInfoTO
     */
    private void deleteTempCart(Long skuId, UserInfoTO userInfoTO) {
        redisTemplate.opsForHash().delete(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserKey(), skuId.toString());
    }

    /**
     * 获取cartKey
     *
     * @return
     */
    private String getString() {
        UserInfoTO userInfoTO = CartInterceptor.toThreadLocal.get();
        String cartKey = "";
        if (!StringUtils.isEmpty(userInfoTO.getUserId())) {
            cartKey = CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserId();
        } else {
            cartKey = CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserKey();
        }
        return cartKey;
    }

    /**
     * 查询购物车列表,并计算总价格,总数量
     *
     * @return
     */
    public CartVO cartList() {
        List<CartItemVO> itemVOS = getCartItemVOS();
        CartVO cartVO = new CartVO();
        Integer totalCount = 0;
        BigDecimal totalMoney = new BigDecimal("0.00");
        for (CartItemVO itemVO : itemVOS) {
            totalCount += itemVO.getSkuCount();
            if (itemVO.getIsChecked() == 0) {
                totalMoney = totalMoney.add(itemVO.getSkuTotalMoney());
            }
        }
        cartVO.setCartItemVOS(itemVOS);
        cartVO.setTotalCount(totalCount);
        cartVO.setTotalMoney(totalMoney);
        cartVO.setCartItemVOS(itemVOS);
        return cartVO;
    }

    /**
     * 获取购物车列表 包含合并购物车操作
     *
     * @return
     */
    private List<CartItemVO> getCartItemVOS() {
        UserInfoTO userInfoTO = CartInterceptor.toThreadLocal.get();
        //获取临时购物车列表
        List<CartItemVO> tempItemVOS = getCartItemVOSByCartKey(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserKey());
        if (StringUtils.isEmpty(userInfoTO.getUserId())) {
            //如果没有登录.直接返回临时购物车数据
            return tempItemVOS;
        } else {
            //临时购物车有数据,合并到长期购物车
            if (tempItemVOS.size() > 0) {
                for (CartItemVO tempItemVO : tempItemVOS) {
                    mergeCart(tempItemVO.getSkuId(), tempItemVO);
                }
            }
            //获取长期购物车列表
            List<CartItemVO> longTempItemVOS = getCartItemVOSByCartKey(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserId());
            return longTempItemVOS;
        }

    }

    /**
     * 根据cartKey获取购物车列表
     *
     * @param cartKey
     * @return
     */
    private List<CartItemVO> getCartItemVOSByCartKey(String cartKey) {
        Map<Object, Object> longTermEntries = redisTemplate.opsForHash().entries(cartKey);
        List<CartItemVO> cartItemVOS = new ArrayList<>();
        Set<Map.Entry<Object, Object>> entries1 = longTermEntries.entrySet();
        for (Map.Entry<Object, Object> objectObjectEntry : entries1) {
            String value = (String) objectObjectEntry.getValue();
            CartItemVO cartItemVO = JSON.parseObject(value, CartItemVO.class);
            cartItemVOS.add(cartItemVO);
        }
        return cartItemVOS;
    }

    public void changeCheck(String skuId, Integer checked) {
        String cartKey = getString();
        Object o = redisTemplate.opsForHash().get(cartKey, skuId);
        String cartItemVOStr = (String) o;
        CartItemVO cartItemVO = JSON.parseObject(cartItemVOStr, CartItemVO.class);
        cartItemVO.setIsChecked(checked);
        redisTemplate.opsForHash().put(cartKey, skuId, JSON.toJSONString(cartItemVO));
    }

    public void changeSkuCount(String skuId, Integer skuCount) {
        String cartKey = getString();
        Object o = redisTemplate.opsForHash().get(cartKey, skuId);
        String cartItemVOStr = (String) o;
        CartItemVO cartItemVO = JSON.parseObject(cartItemVOStr, CartItemVO.class);
        cartItemVO.setSkuCount(skuCount);
        cartItemVO.setSkuTotalMoney(cartItemVO.getSkuPrice().multiply(new BigDecimal(skuCount)));
        redisTemplate.opsForHash().put(cartKey, skuId, JSON.toJSONString(cartItemVO));

    }

    public void deleteItemCartVO(String skuId) {
        String cartKey = getString();
        redisTemplate.opsForHash().delete(cartKey, skuId);
    }

    public ResultBody<List<CartItemVO>> getCartListByUid(String uid) {
        Map<Object, Object> longTermEntries = redisTemplate.opsForHash().entries(CartConstant.CART_REDIS_PREFIX + uid);
        List<CartItemVO> cartItemVOS = new ArrayList<>();
        Set<Map.Entry<Object, Object>> entries1 = longTermEntries.entrySet();
        for (Map.Entry<Object, Object> objectObjectEntry : entries1) {
            String value = (String) objectObjectEntry.getValue();
            CartItemVO cartItemVO = JSON.parseObject(value, CartItemVO.class);
            //获取选中的
            if (cartItemVO.getIsChecked() == 0) {
                ResultBody<SkuInfoVO> info = productServiceAPI.info(cartItemVO.getSkuId());
                SkuInfoVO skuInfoVO = info.getData();
                cartItemVO.setSkuPrice(skuInfoVO.getPrice());
                cartItemVO.setSkuTotalMoney(skuInfoVO.getPrice().multiply(new BigDecimal(cartItemVO.getSkuCount())));
                cartItemVOS.add(cartItemVO);
            }
        }

        return new ResultBody<>(cartItemVOS);
    }

    //从session获取购userInfo
    public ResultBody<List<CartItemVO>> getCartList() {
        UserInfoTO userInfoTO = CartInterceptor.toThreadLocal.get();
        Map<Object, Object> longTermEntries = redisTemplate.opsForHash().entries(CartConstant.CART_REDIS_PREFIX + userInfoTO.getUserId());
        List<CartItemVO> cartItemVOS = new ArrayList<>();
        Set<Map.Entry<Object, Object>> entries1 = longTermEntries.entrySet();
        for (Map.Entry<Object, Object> objectObjectEntry : entries1) {
            String value = (String) objectObjectEntry.getValue();
            CartItemVO cartItemVO = JSON.parseObject(value, CartItemVO.class);
            //获取选中的
            if (cartItemVO.getIsChecked() == 0) {
                cartItemVOS.add(cartItemVO);
            }
        }

        return new ResultBody<>(cartItemVOS);


    }
}
