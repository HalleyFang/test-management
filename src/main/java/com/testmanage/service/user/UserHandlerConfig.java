package com.testmanage.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class UserHandlerConfig extends WebMvcConfigurationSupport {

    @Autowired
    UserHandler userHandler;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.userHandler).excludePathPatterns("/login").excludePathPatterns("/error");
        super.addInterceptors(registry);
    }

}
