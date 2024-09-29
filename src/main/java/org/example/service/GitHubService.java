package org.example.service;

import org.example.entity.GitHubUser;
import org.example.entity.Repo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {
    private final RestTemplate restTemplate;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // loads the data from API
    public Repo[] getGitHubOrgRepos(String org, int page) {
        String url = String.format("https://api.github.com/orgs/%s/repos?per_page=100?page=%s", org, page);
        return restTemplate.getForObject(url, Repo[].class);
    }

    public GitHubUser[] getRepoContributors(String owner, String repo, int page) {
        String url = String.format("https://api.github.com/repos/%s/%s/contributors?per_page=100&page=%s", owner, repo, page);
        return restTemplate.getForObject(url, GitHubUser[].class);
    }
}