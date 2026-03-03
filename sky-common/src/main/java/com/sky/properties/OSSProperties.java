package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:oss.properties")
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {
    String endpoint;
    String bucketName;
    String region;
}