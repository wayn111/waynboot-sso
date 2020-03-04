package com.wayn.ssoserver.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Controller
public class CaptchaController {

    @Autowired
    private Producer producer;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        try (ServletOutputStream out = response.getOutputStream()) {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");

            String capText = producer.createText();
            HttpSession session = request.getSession();
            //将验证码存入shiro 登录用户的session
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
            BufferedImage image = producer.createImage(capText);
            ImageIO.write(image, "jpg", out);
            out.flush();
        } catch (IOException e) {
            log.error("验证码生成失败", e);
        }

    }
}
