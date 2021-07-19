package com.testmanage.controller;

import com.testmanage.entity.MyUser;
import com.testmanage.utils.JsonParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    @RequestMapping(path = "/login", method = {RequestMethod.POST},
            produces = "application/json;charset=UTF-8")
    public Map<String, Object> login(@RequestBody String body, HttpServletResponse response) {
        MyUser user = JsonParse.getGson().fromJson(JsonParse.StringToJson(body), MyUser.class);
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            result.put("status", 400);
            result.put("msg", "请输入用户名和密码！");
            return result;
        }
        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUsername(),
                user.getPassword()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            result.put("status", 500);
            result.put("msg", "用户名不存在！");
            return result;
        } catch (AuthenticationException e) {
            log.error("账号或密码错误！", e);
            result.put("status", 500);
            result.put("msg", "账号或密码错误！");
            return result;
        } catch (AuthorizationException e) {
            result.put("status", 401);
            result.put("msg", "没有权限！");
            return result;
        }
        result.put("status", 200);
        result.put("msg", "登录成功！");
        result.put("username", user.getUsername());
        result.put("roles", user.getRoles());
        result.put("permissions", user.getPermissions());
        return result;
    }

    @RequiresRoles("admin")
    @GetMapping("/admin")
    public String admin() {
        return "admin success!";
    }

    @RequiresPermissions("query")
    @GetMapping("/index")
    public String index() {
        return "index success!";
    }

    @RequiresPermissions("add")
    @GetMapping("/add")
    public String add() {
        return "add success!";
    }


}
