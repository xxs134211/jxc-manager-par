package com.xxs.jxcadmin.interceptors;

import com.xxs.jxcadmin.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 13421
 */
public class NoLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        User user = (User)request.getSession().getAttribute("user");

        if(null == user){
            response.sendRedirect("index");
            return false;
        }
        return true;
    }
}
