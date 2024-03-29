package com.xxs.jxcadmin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.jxcadmin.model.RespBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
@Component
public class JxcAuthenticationFailedHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(
                    RespBean.error("用户名或密码错误!!!!!")));
    }
}
