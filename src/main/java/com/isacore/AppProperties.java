package com.isacore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private List<String> urls;
    private String jwtUri;
}
