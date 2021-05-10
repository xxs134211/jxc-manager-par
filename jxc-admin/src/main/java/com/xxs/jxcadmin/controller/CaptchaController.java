package com.xxs.jxcadmin.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xxs.jxcadmin.model.CaptchaImageModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author 13421
 */
@RestController
public class CaptchaController {
    @Resource
    public DefaultKaptcha defaultKaptcha;

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public  void kaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control","post-check=0, pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/jpeg");

        String capText = defaultKaptcha.createText();
        session.setAttribute("captcha_key",new CaptchaImageModel(capText, 2*60));
        ServletOutputStream os = response.getOutputStream();
        BufferedImage bufferedImage = defaultKaptcha.createImage(capText);
        ImageIO.write(bufferedImage,"jpg",os);
        os.flush();
    }
}
