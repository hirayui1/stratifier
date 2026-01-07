package org.example.service;


import org.example.entity.Repo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    // gets org repo names, invoked parallelly
    public Flux<Repo> getOrgRepoNames(String org) {
        String uri = String.format("https://api.github.com/orgs/%s/repos", org);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Repo.class);
    }
}