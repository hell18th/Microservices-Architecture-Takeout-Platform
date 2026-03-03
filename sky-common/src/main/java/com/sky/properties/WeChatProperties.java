package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:wx.properties")
@ConfigurationProperties(prefix = "wx")
public class WeChatProperties {
    private String appid;
    private String secret;
}