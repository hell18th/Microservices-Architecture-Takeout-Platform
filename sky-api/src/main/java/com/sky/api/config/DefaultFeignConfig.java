package com.sky.api.config;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return template -> {
            String path = template.path();
            Long id = BaseContext.getCurrentId();
            if (id == null) {
                return;
            }
            if (path.startsWith("/admin")) {
                template.header(JwtClaimsConstant.EMP_ID, id.toString());
            }
            if (path.startsWith("/user")) {
                template.header(JwtClaimsConstant.USER_ID, id.toString());
            }
        };
    }
}