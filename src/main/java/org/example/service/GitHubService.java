package org.example.service;

import org.example.entity.GitHubUser;
import org.example.entity.Repo;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

@Service
public class GitHubService {
    private final WebClient webClient;

    public GitHubService(WebClient webClient) {
        this.webClient = webClient;
    }

    // gets org info,

    public Mono<String> getOrgAPICall(String org) {
        String uri = String.format("https://api.github.com/orgs/%s", org);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }

    // gets org repo names, unparalleled call with superior parallel request
    public Flux<String> getOrgRepoNames(String org) {
        String uri = String.format("https://api.github.com/orgs/%s/repos", org);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(String.class);
    }

    // loads the data from API

}