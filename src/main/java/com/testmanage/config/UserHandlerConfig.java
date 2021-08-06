package com.testmanage.config;

import com.testmanage.service.user.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class UserHandlerConfig extends WebMvcConfigurationSupport {

    @Autowired
    UserHandler userHandler;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.userHandler)
                .excludePathPatterns("/login")
                .excludePathPatterns("/error")
        .excludePathPatterns("/css/**").excludePathPatterns("/js/**").excludePathPatterns("/fonts/**").excludePathPatterns("/img/**");
        super.addInterceptors(registry);
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
    }

}
