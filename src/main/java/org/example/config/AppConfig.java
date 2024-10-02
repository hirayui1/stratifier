package org.example.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(gitHubTokenInterceptor()));
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        return restTemplate;
    }

    private ClientHttpRequestInterceptor gitHubTokenInterceptor() {
        return (request, body, execution) -> {
           request.getHeaders().add("Authorization", "token " + token);
           return execution.execute(request, body);
        };
    }
}
