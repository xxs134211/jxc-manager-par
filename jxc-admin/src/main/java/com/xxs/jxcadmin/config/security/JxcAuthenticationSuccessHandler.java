package com.xxs.jxcadmin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.jxcadmin.model.RespBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
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
public class JxcAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(
                RespBean.success("登录成功")));
    }
}
