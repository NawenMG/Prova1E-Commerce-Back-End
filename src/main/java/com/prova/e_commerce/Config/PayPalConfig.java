package com.prova.e_commerce.Config;

import com.paypal.base.rest.APIContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    private final String clientId = "AUy8S5YDOc28ad_l1Bq-FEmtz5nDhT0hdgH-IGXjO4dd2ofWSYRSJZUAKZZFipsoJGytV1eh2-8bvPtG"; 
    private final String clientSecret = "EJ4WR8VTqHe-rud6Hhh9nq6Ip7oSH9ZAj4JA8DUKS-L3ubsupiuHtEBXJaXVAjgzABSqKuekNWnVP1Ln"; 
    private final String mode = "sandbox"; 

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}
