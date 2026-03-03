package com.sky.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private String adminPath;
    private String userPath;
    private List<String> adminExcludePaths;
    private List<String> userExcludePaths;
}