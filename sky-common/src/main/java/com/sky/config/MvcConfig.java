package com.sky.config;

import com.sky.interceptor.TokenInterceptor;
import com.sky.interceptor.UserTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(WebMvcConfigurer.class)
public class MvcConfig implements WebMvcConfigurer {
    private final TokenInterceptor tokenInterceptor;
    private final UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(userTokenInterceptor).addPathPatterns("/user/**");
    }
}