package com.testmanage.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testmanage.entity.MyUser;
import com.testmanage.service.user.UserConfService;
import com.testmanage.service.user.UserContext;
import com.testmanage.service.user.UserService;
import com.testmanage.utils.JsonParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserConfService userConfService;

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public void addUser(@RequestBody String body) throws Exception {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        MyUser user = JsonParse.getGson().fromJson(bodyJson, MyUser.class);
        userService.addUser(user);
    }

    @PostMapping("/updateUser")
    public void updateUser(@RequestBody String body) throws Exception {
        JsonObject bodyJson = JsonParse.StringToJson(body);
        MyUser user = JsonParse.getGson().fromJson(bodyJson, MyUser.class);
        userService.updateUser(user, null);
    }

    @PostMapping("/changePwd")
    public HttpServletResponse changePassword(@RequestBody String body, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        JsonObject bodyJson = JsonParse.StringToJson(body);
        String oldPwd = bodyJson.get("oldPass").getAsString();
        String newPwd = bodyJson.get("newPass").getAsString();
        String confirmNewPwd = bodyJson.get("confirmNewPass").getAsString();
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(confirmNewPwd)) {
            result.put("status", 500);
            result.put("msg", "密码不能为空");
        } else {
            String username = UserContext.get().getUsername();
            MyUser myUser = userService.getUserByName(username);
            if (!(myUser instanceof MyUser)) {
                result.put("status", 500);
                result.put("msg", "用户不存在");
            } else {
                //校验原密码
                Md5Hash md5Hash = new Md5Hash(oldPwd, ByteSource.Util.bytes(myUser.getSalt()), 1024);
                if (!md5Hash.toHex().equals(myUser.getPassword())) {
                    result.put("status", 500);
                    result.put("msg", "原密码不正确");
                } else if (oldPwd.equals(newPwd)) {
                    result.put("status", 500);
                    result.put("msg", "新密码不能与原密码一致");
                } else if (newPwd.length() < 6) {
                    result.put("status", 500);
                    result.put("msg", "新密码长度至少为6位");
                } else {
                    //校验新密码两次是否一致
                    if (!newPwd.equals(confirmNewPwd)) {
                        result.put("status", 500);
                        result.put("msg", "新密码两次输入不一致");
                    } else {
                        //新密码user
                        myUser.setPassword(newPwd);
                        userService.updateUser(myUser, "changePassword");
                    }
                }
            }
        }

        String data = JsonParse.getGson().toJson(result);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] dataByteArr = data.getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/avatar")
    public void avatar(HttpServletResponse response) throws IOException {
        OutputStream os = null;
        try {
            String path = userConfService.getAvatar(UserContext.get().getUsername());
            if (path == null || path.isEmpty()) {
                path = "static/img/avatar.png";
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
    public void users(HttpServletResponse response) {
        List<MyUser> list = userService.getUsersIgnoreAdmin();
        JsonArray jsonArray = new JsonArray();
        for (MyUser user : list) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("label", user.getUsername());
            jsonObject.addProperty("value", user.getUsername());
            jsonArray.add(jsonObject);
        }
        String data = JsonParse.getGson().toJson(jsonArray);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
