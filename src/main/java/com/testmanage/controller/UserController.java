package com.testmanage.controller;

import com.testmanage.service.user.UserConfService;
import com.testmanage.service.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserConfService userConfService;

    @GetMapping("/avatar")
    public void avatar(HttpServletResponse response) throws IOException {
        OutputStream os = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/img/avatar.png");
            BufferedImage image = ImageIO.read(classPathResource.getInputStream());
            response.setContentType("image/png");
            os = response.getOutputStream();

            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (IOException e) {
            log.error("获取图片异常:{}", e.getMessage());
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }

    @GetMapping("/userConf/getV")
    public String getV() {
        String value = userConfService.getV(UserContext.get().getUsername());
        return value;
    }

    @PostMapping("/userConf/setV")
    public boolean setV(@RequestParam String value) {
        userConfService.setV(UserContext.get().getUsername(), value);
        return true;
    }
}
