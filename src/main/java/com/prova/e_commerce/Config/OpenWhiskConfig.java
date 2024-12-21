package com.prova.e_commerce.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenWhiskConfig {

    @Value("${openwhisk.host}")
    private String openWhiskHost;

    @Value("${openwhisk.auth}")
    private String openWhiskAuth;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getBaseUrl() {
        return openWhiskHost;
    }

    public String getAuth() {
        return openWhiskAuth;
    }
}
