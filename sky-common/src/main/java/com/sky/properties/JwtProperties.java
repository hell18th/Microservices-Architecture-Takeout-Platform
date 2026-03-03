package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:jwt.properties")
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String adminTokenName;

    private String userTokenName;

    private String secretKey;

    private Long expireTime;
}