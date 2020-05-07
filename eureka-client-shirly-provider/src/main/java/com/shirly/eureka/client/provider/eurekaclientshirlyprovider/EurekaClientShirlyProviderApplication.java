package com.shirly.eureka.client.provider.eurekaclientshirlyprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EurekaClientShirlyProviderApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(EurekaClientShirlyProviderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
