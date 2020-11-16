package com.iflytek.gulimall.autherserver.web;

import com.alibaba.fastjson.JSON;
import com.iflytek.gulimall.common.feign.MemberServiceAPI;
import com.iflytek.gulimall.common.feign.vo.Auth2SocialVO;
import com.iflytek.gulimall.common.feign.vo.MemberVO;
import com.iflytek.gulimall.common.utils.CookieUtils;
import com.iflytek.gulimall.common.utils.HttpUtils;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.autherserver.component.Auth2WeiBoComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.iflytek.gulimall.common.constant.AutherServerConstant.*;

@Controller
@RequestMapping("auth2")
public class OAuth2LoginController {

    @Autowired
    private Auth2WeiBoComponent auth2WeiBoComponent;

    @Autowired
    MemberServiceAPI memberServiceAPI;

    @GetMapping("/weibo/login/success")
    public String auth2WeiBoLoginSuccess(@RequestParam("code") String code,
                                         RedirectAttributes redirectAttributes,
                                         HttpServletRequest request,
                                         HttpSession session) throws UnsupportedEncodingException {
        if (session.getAttribute("loginUser") != null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", "您已经登录,无需重复登录");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:" + URL_LOGIN;
        }
        Map<String, String> requestParam = new HashMap<>();
        //https://api.weibo.com/oauth2/access_token?client_id=833884090&client_secret=591a1eb2d83e3f0056e3356ed9b8afd4&
        // grant_type=authorization_code&redirect_uri=http://gulimall.com/success&code=1adec408cbe51aca908075f1bf4daaa7
        requestParam.put("client_id", auth2WeiBoComponent.getClient_id());
        requestParam.put("client_secret", auth2WeiBoComponent.getClient_secret());
        requestParam.put("grant_type", auth2WeiBoComponent.getGrant_type());
        requestParam.put("redirect_uri", auth2WeiBoComponent.getRedirect_uri());
        requestParam.put("code", code);
        String resultStr = HttpUtils.sendPost(auth2WeiBoComponent.getRequest_url(), requestParam);
        Auth2SocialVO auth2VO = JSON.parseObject(resultStr, Auth2SocialVO.class);
        if (!StringUtils.isEmpty(auth2VO.getAccess_token())) {
            //调用会员登录
            auth2VO.setType(1);
            ResultBody<MemberVO> memberVOResultBody = memberServiceAPI.auth2Login(auth2VO);
            if (0 == memberVOResultBody.getCode()) {
                MemberVO memberVO = memberVOResultBody.getData();
                session.setAttribute(LOGIN_USER, memberVO);
            } else {
                String msg = memberVOResultBody.getMsg();
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", msg);
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:" + URL_LOGIN;
            }
            //登录成功之后应该跳转的地址从cookie中获取
            String fromurl = CookieUtils.getCookieValue(request, SOCIALLOGIN_RETURNURL_COOKIENAME, true);
            String decodefFromurl = URLDecoder.decode(fromurl, "utf-8");
            return "redirect:" + decodefFromurl;
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", "登录失败.请重新登录");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:" + URL_LOGIN;

        }
    }


}
