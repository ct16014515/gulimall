package com.iflytek.gulimall.autherserver.controller;

import com.iflytek.common.constant.AutherServerConstant;
import com.iflytek.common.model.vo.memeber.MemberVO;
import com.iflytek.common.model.vo.memeber.UserLoginVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.autherserver.vo.RegisterVO;
import com.iflytek.gulimall.autherserver.feign.MemberService;
import com.iflytek.gulimall.autherserver.feign.ThirdPartyService;
import com.iflytek.gulimall.autherserver.util.HttpUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MemberService memberService;

    @Autowired
    RedissonClient redissonClient;

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
        RLock phoneLock = redissonClient.getLock(phone);
        /**
         *waitTime 用此时间查看是否有锁,如果太长会导致线程等待
         *leaseTime 上锁时间,即此时间过了之后会释放锁,redis此条无记录
         */
        boolean b = phoneLock.tryLock(100, 60000L, TimeUnit.MILLISECONDS);
        if (!b) {
            return new ResultBody(10001, "发送频繁", null);
        }
        String ipAddress = HttpUtils.getIpAddress(request);
        RLock ipLock = redissonClient.getLock(ipAddress);
        //对ip进行防刷,防止同一ip发送不同请求
        boolean ipflag = ipLock.tryLock(100, 20000L, TimeUnit.MILLISECONDS);
        if (!ipflag) {
            return new ResultBody(10001, "发送频繁", null);
        }
        String code = (Math.random() + "").substring(2, 8);
        redisTemplate.opsForValue().set(
                AutherServerConstant.SMS_CODE_CACHE_PREFIX + phone,
                code,
                20,
                TimeUnit.MINUTES);
        ResultBody resultBody = thirdPartyService.sendMsg(phone, code);
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
    public String register(@Valid RegisterVO registerVO, BindingResult result, RedirectAttributes redirectAttributes) {
        //1、数据校验
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            Map<String, String> errors = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com:9006/register.html";
        }
        //2、校验验证码
        String phone = registerVO.getPhone();
        String key = AutherServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisCode = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(redisCode) && registerVO.getCode().equals(redisCode)) {
            redisTemplate.delete(key);
            ResultBody resultBody = memberService.memberRegister(registerVO);
            if (0 == resultBody.getCode()) {
                return "redirect:http://auth.gulimall.com:9006/login.html";
            } else {
                String msg = resultBody.getMsg();
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", msg);
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com:9006/register.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com:9006/register.html";
        }
    }

    /**
     * 用户登录
     *
     * @param userLoginVO
     * @param redirectAttributes
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginVO userLoginVO, RedirectAttributes redirectAttributes, HttpSession session) {
        ResultBody<MemberVO> memberVOResultBody = memberService.memberLogin(userLoginVO);
        if (0 != memberVOResultBody.getCode()) {
            Map<String, String> errors = new HashMap<>();
            String msg = memberVOResultBody.getMsg();
            errors.put("msg", msg);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com:9006/login.html";
        } else {
            //登录成功
            MemberVO memberVO = memberVOResultBody.getData();
            session.setAttribute(AutherServerConstant.LOGIN_USER, memberVO);
            return "redirect:http://gulimall.com:9006";
        }
    }

    @GetMapping("/login.html")
    public String login(HttpSession session) {
        Object attribute = session.getAttribute(AutherServerConstant.LOGIN_USER);
        if (attribute == null) {
            return "login";
        } else {
            return "redirect:http://gulimall.com:9006";
        }
    }


}
