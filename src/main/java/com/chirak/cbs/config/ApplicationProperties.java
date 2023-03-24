package com.chirak.cbs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cbs.jwt")
@Data
public class ApplicationProperties {
    private String expiresAfter;
    private String secret_key;
}