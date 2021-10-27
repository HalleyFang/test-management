package com.testmanage.service.user;

import com.testmanage.entity.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class UserHandler implements HandlerInterceptor {

    @Autowired
    UserConfService userConfService;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String path = request.getRequestURI();
        /*if(path.startsWith("/api/")){
            path = path.substring(4);
            try {
                request.getRequestDispatcher(path).forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        */

        if (path.equalsIgnoreCase("/auth/login")) {
            return true;
        }
        if (path.equalsIgnoreCase("/addAutoCase")) {
            return true;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            String username = subject.getPrincipal().toString();
            MyUser myUser = new MyUser(username,
                    userConfService.getV(username));
            UserContext.set(myUser);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("拦截器出错", e);
            log.error("出错的请求：" + path);
            return Boolean.FALSE;
        }
    }

    /**
     * 请求处理之后进行调用（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mv)
            throws Exception {
    }

    /**
     * 在整个请求结束之后被调用（主要是用于进行资源清理工作）
     * 一定要在请求结束后调用remove清除当前线程的副本变量值，否则会造成内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex)
            throws Exception {
        UserContext.remove();
    }

}
