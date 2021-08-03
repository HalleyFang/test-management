package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testmanage.entity.MyUser;
import com.testmanage.service.user.UserConfService;
import com.testmanage.service.user.UserContext;
import com.testmanage.service.user.UserService;
import com.testmanage.utils.JsonParse;
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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserConfService userConfService;

    @Autowired
    UserService userService;

    @GetMapping("/avatar")
    public void avatar(HttpServletResponse response) throws IOException {
        OutputStream os = null;
        try {
            String path = userConfService.getAvatar(UserContext.get().getUsername());
            if(path==null || path.isEmpty()){
                path="static/img/avatar.png";
            }
            ClassPathResource classPathResource = new ClassPathResource(path);
            BufferedImage image = ImageIO.read(classPathResource.getInputStream());
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (IOException e) {
            log.error("获取图片异常:{}", e.getMessage());
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

    @GetMapping("/users")
    public void users(HttpServletResponse response){
        List<MyUser> list = userService.getUsersIgnoreAdmin();
        JsonArray jsonArray = new JsonArray();
        for (MyUser user : list){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("label",user.getUsername());
            jsonObject.addProperty("value",user.getUsername());
            jsonArray.add(jsonObject);
        }
        String data = JsonParse.getGson().toJson(jsonArray);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
