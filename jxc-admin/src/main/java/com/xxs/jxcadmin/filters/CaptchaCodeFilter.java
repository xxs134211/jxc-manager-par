package com.xxs.jxcadmin.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.jxcadmin.model.CaptchaImageModel;
import com.xxs.jxcadmin.model.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 13421
 */
@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/login",request.getRequestURI())&& StringUtils.equalsAnyIgnoreCase(request.getMethod(),"post")){
            try {
                this.validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(OBJECT_MAPPER.writeValueAsString(
                        RespBean.error(e.getMessage())));
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "captchaCode");
        if(StringUtils.isEmpty(codeInRequest)){
            throw new SessionAuthenticationException("验证码不能为空！");
        }
        CaptchaImageModel codeInSession = (CaptchaImageModel) session.getAttribute("captcha_key");
        if(Objects.isNull(codeInSession)){
            throw new SessionAuthenticationException("验证码不存在！");
        }

        if(codeInSession.isExpired()){
            throw new SessionAuthenticationException("验证码已过期！");
        }
        if(!StringUtils.equals(codeInRequest,codeInSession.getCode())){
            throw new SessionAuthenticationException("验证码错误！");
        }
    }
}
