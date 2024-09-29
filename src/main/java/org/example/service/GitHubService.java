package org.example.service;

import org.example.entity.GitHubUser;
import org.example.entity.Organization;
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
    public Organization getGitHubOrgProfile(String org) {
        String url = String.format("https://api.github.com/orgs/%s", org);
        return restTemplate.getForObject(url, Organization.class);
    }

    public Repo[] getGitHubOrgRepos(String org) {
        String url = String.format("https://api.github.com/orgs/%s/repos", org);
        return restTemplate.getForObject(url, Repo[].class);
    }

    public GitHubUser[] getRepoContributors(String owner, String repo) {
        String url = String.format("https://api.github.com/repos/%s/%s/contributors", owner, repo);
        return restTemplate.getForObject(url, GitHubUser[].class);
    }
}
