package com.iflytek.gulimall.autherserver.web;

import com.iflytek.gulimall.common.constant.AutherServerConstant;

import com.iflytek.gulimall.common.feign.MemberServiceAPI;
import com.iflytek.gulimall.common.feign.ThirdPartyServiceAPI;
import com.iflytek.gulimall.common.feign.vo.MemberVO;
import com.iflytek.gulimall.common.feign.vo.RegisterVO;
import com.iflytek.gulimall.common.feign.vo.UserLoginVO;

import com.iflytek.gulimall.common.utils.HttpUtils;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.common.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.iflytek.gulimall.common.constant.AutherServerConstant.*;


@Controller
@Slf4j
public class LoginController {
    @Autowired
    private ThirdPartyServiceAPI thirdPartyServiceAPI;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MemberServiceAPI memberServiceAPI;
    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("sms/sendCode")
    @ResponseBody
    public ResultBody sendMsg(@RequestParam("phone") String phone, HttpServletRequest request) throws InterruptedException {
        //一个手机号在60s内只能发送一次
//        String value = redisTemplate.opsForValue().get(AutherServerConstant.SMS_CODE_CACHE_PREFIX + phone);
//        if (!StringUtils.isEmpty(value)) {
//            //判断时间是否小于60秒
//            long begin = Long.parseLong(value.split("_")[1]);
//            long end = System.currentTimeMillis();
//            if (end - begin <60000) {
//                return new ResultBody(10001, "发送频繁", null);
//            }
//        }

//        RLock phoneLock = redissonClient.getLock(AutherServerConstant.REDISSON_LOCK_PREFIX + "phone:" + phone);
//        /**
//         *waitTime 用此时间查看是否有锁,如果太长会导致线程等待
//         *leaseTime 上锁时间,即此时间过了之后会释放锁,redis此条无记录
//         */
//        boolean b = phoneLock.tryLock(100, 60000L, TimeUnit.MILLISECONDS);
//        if (!b) {
//            return new ResultBody(10001, "发送频繁", null);
//        }
        /**
         *waitTime 用此时间查看是否有锁,如果太长会导致线程等待
         *leaseTime 上锁时间,即此时间过了之后会释放锁,redis此条无记录
         */
        String ipAddress = HttpUtils.getIpAddress(request);
        RLock ipLock = redissonClient.getLock(AutherServerConstant.REDISSON_LOCK_PREFIX + "ip:" + ipAddress);
        //对ip进行防刷,防止同一ip发送不同请求
        boolean ipflag = ipLock.tryLock(100, 60000L, TimeUnit.MILLISECONDS);
        if (!ipflag) {
            return new ResultBody(10001, "发送频繁", null);
        }
        String code = (Math.random() + "").substring(2, 8);
        redisTemplate.opsForValue().set(
                AutherServerConstant.SMS_CODE_CACHE_PREFIX + phone,
                code,
                20,
                TimeUnit.MINUTES);
        ResultBody resultBody = thirdPartyServiceAPI.sendMsg(phone, code);
        return resultBody;
    }

    public static void main(String[] args) {
        String s = (Math.random() + "").substring(2, 8);
        System.out.println(s);
    }

    /**
     * 用户注册
     * Model 转发上下文携带数据
     * RedirectAttributes 为重定向上下文携带数据
     *
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid RegisterVO registerVO,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        //1、数据校验
        if (result.hasErrors()) {
            Map<String, String> errors = ValidationUtils.validationErrors(result);
            redirectAttributes.addFlashAttribute("errors", errors);
            //如果是转发到register,会导致表单重复提交
            // return  "register";
            return "redirect:" + AutherServerConstant.URL_REGISTER;
        }
        //2、校验验证码
        String phone = registerVO.getPhone();
        String key = AutherServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisCode = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(redisCode) && registerVO.getCode().equals(redisCode)) {
            //redisTemplate.delete(key);
            ResultBody resultBody = memberServiceAPI.memberRegister(registerVO);
            if (0 == resultBody.getCode()) {
                return "redirect:" + URL_LOGIN;
            } else {
                String msg = resultBody.getMsg();
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", msg);
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:" + AutherServerConstant.URL_REGISTER;
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:" + AutherServerConstant.URL_REGISTER;
        }
    }

    @GetMapping("/login.html")
    public String loginHtml() {
        return "login";
    }


//    /**
//     * @param userLoginVO
//     * @return
//     */
//    @PostMapping("/login")
//    public String userLogin(@Valid UserLoginVO userLoginVO,
//                            BindingResult result,
//                            HttpServletRequest request,
//                            HttpServletResponse response,
//                            RedirectAttributes redirectAttributes) {
//        log.info("请求参数" + userLoginVO);
//
//
//        if (result.hasErrors()) {
//            Map<String, String> errors = ValidationUtils.validationErrors(result);
//            redirectAttributes.addFlashAttribute("errors", errors);
//            return "redirect:http://auth.gulimall.com/login.html";
//        }
//        ResultBody<MemberVO> memberVOResultBody = memberService.memberLogin(userLoginVO);
//        if (0 == memberVOResultBody.getCode()) {
//            MemberVO memberVO = memberVOResultBody.getData();
//            //将用户名和密码放入载荷
//            Map<String, Object> map = new HashMap<>();
//            map.put("username", memberVO.getUsername());
//            map.put("userId", memberVO.getUserId());
//            //防止jwt被盗取,增加ip限制
//            String ip = HttpUtils.getIpAddress(request);
//            map.put("ip", ip);
//            String jwtToken = JwtUtils.generateToken(map, jwtConfig.getPrivateKey(), jwtConfig.getExpire());
//            //jwt写入cookie只读属性
//            CookieUtils.setCookie(request,
//                    response,
//                    jwtConfig.getJwtCookieName(),
//                    jwtToken,
//                    jwtConfig.getExpire(),
//                    "utf-8",
//                    true);
//            //用户姓名写入cookie
//            CookieUtils.setCookie(request, response, jwtConfig.getUnick(), memberVO.getUsername(), jwtConfig.getExpire() * 60);
//            if (StringUtils.isEmpty(userLoginVO.getReturnUrl())) {
//                Map<String, String> success = new HashMap<>();
//                success.put("msg", "登录成功");
//                redirectAttributes.addFlashAttribute("errors", success);
//                return "redirect:http://auth.gulimall.com/login.html";
//            } else {
//                return "redirect:" + userLoginVO.getReturnUrl();
//            }
//        } else {
//
//            String msg = memberVOResultBody.getMsg();
//            Map<String, String> errors = new HashMap<>();
//            errors.put("msg", msg);
//            redirectAttributes.addFlashAttribute("errors", errors);
//            return "redirect:http://auth.gulimall.com/login.html";
//        }
//    }


    /**
     * @param userLoginVO
     * 1在搜索页面跳转至登录页面时,由于returnurl包含中文,js需要编码,防止重定向到returnurl出现中文乱码问题.
     * 2在搜索页面跳转至登录页面,springmvc会自动把参数解码,如果前端js参数编码了,后台解码,会导致新的参数returnUrl解码,
     *   当登录失败时,重定到登录页的returnUrl是处于解码状态,再次登录成功出现中文乱码.
     * @return
     */
    @PostMapping("/login")
    public String userLogin(@Valid UserLoginVO userLoginVO,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) throws UnsupportedEncodingException {
        String redirectLogin = "";
        if (!StringUtils.isEmpty(userLoginVO.getReturnUrl())) {
            String encodeReturnUrl = URLEncoder.encode(userLoginVO.getReturnUrl(), "utf-8");
            System.out.println("回调地址为:"+encodeReturnUrl);
            redirectLogin = "redirect:" + URL_LOGIN + "?returnUrl=" + encodeReturnUrl;
        }else {
            redirectLogin = "redirect:" + URL_LOGIN ;
        }
        if (result.hasErrors()) {
            Map<String, String> errors = ValidationUtils.validationErrors(result);
            redirectAttributes.addFlashAttribute("errors", errors);
            return redirectLogin;
        }
        if (session.getAttribute("loginUser") != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", "您已经登录,无需重复登录");
            redirectAttributes.addFlashAttribute("errors", errors);
            return redirectLogin;
        }

        ResultBody<MemberVO> memberVOResultBody = memberServiceAPI.memberLogin(userLoginVO);
        if (memberVOResultBody.getCode() == 0) {
            MemberVO memberVO = memberVOResultBody.getData();
            session.setAttribute(LOGIN_USER, memberVO);
            if (StringUtils.isEmpty(userLoginVO.getReturnUrl())) {
                return "redirect:" + URL_PORTAL;
            } else {
                return "redirect:" + userLoginVO.getReturnUrl();
            }
        } else {
            String msg = memberVOResultBody.getMsg();
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", msg);
            redirectAttributes.addFlashAttribute("errors", errors);
            return redirectLogin;
        }
    }

    @GetMapping("logout.html")
    public String logout(@RequestParam(value = "returnUrl",required = false) String returnUrl, HttpSession session) {
        session.removeAttribute(LOGIN_USER);
        if (StringUtils.isEmpty(returnUrl)){
            returnUrl=URL_PORTAL;
        }
        return "redirect:" + returnUrl;
    }


}



