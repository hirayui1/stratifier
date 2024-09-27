package org.example;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:config.properties")
public class AppConfig {

    @Value("${token}")
    private String token;

    @PostConstruct // print token to check if properly loaded from env var
    public void printToken() {
        System.out.println("\nToken: " + token + "\n");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
