package org.example.controller;

import org.example.entity.Repo;
import org.example.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/github")
public class GitHubController {
    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("org/{org}")
    public Mono<String> getOrg(@PathVariable String org) {
        return gitHubService.getOrgAPICall(org);
    }

    @GetMapping("org/{org}/repos")
    public List<Repo> getOrgRepoNames(@PathVariable String org) {
        return fetchRepoNames(org);
    }

    private List<Repo> fetchRepoNames(String org) { // Parallel vs Flux
        return null;
    }
}