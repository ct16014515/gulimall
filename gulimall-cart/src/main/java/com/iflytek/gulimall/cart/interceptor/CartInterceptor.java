package com.iflytek.gulimall.cart.interceptor;

import com.iflytek.common.model.vo.memeber.MemberVO;
import com.iflytek.common.utils.CookieUtils;
import com.iflytek.gulimall.cart.to.UserInfoTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static com.iflytek.common.constant.AutherServerConstant.LOGIN_USER;

public class CartInterceptor implements HandlerInterceptor {

   public static ThreadLocal<UserInfoTO> toThreadLocal=new ThreadLocal<>();


    /**
     * 1、从session里面判断是临时用户还是登陆用户
     * 2、如果是临时用户从cookie获取userkey 若cookie照中没有
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object sessionAttribute = session.getAttribute(LOGIN_USER);
        UserInfoTO userInfoTO = new UserInfoTO();
        if (sessionAttribute != null) {
            MemberVO memberVO = (MemberVO) sessionAttribute;
            Long userId = memberVO.getUserId();
            userInfoTO.setUserId(userId.toString());
        }
        String userKey = CookieUtils.getCookieValue(request, "user-key");
        if (!StringUtils.isEmpty(userKey)) {
            userInfoTO.setUserKey(userKey);
        } else {
            userInfoTO.setUserKey(UUID.randomUUID().toString());
        }
        toThreadLocal.set(userInfoTO);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTO userInfoTO = toThreadLocal.get();
        CookieUtils.setCookie(request,
                response,
                "user-key",
                userInfoTO.getUserKey(),
                60 * 60 * 24 * 30,
                "utf-8",
                true);
    }
}
