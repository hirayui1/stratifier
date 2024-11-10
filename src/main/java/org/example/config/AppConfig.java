package org.example.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

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
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("Authorization", "token" + token)
                .build();
    }
}