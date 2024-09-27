package org.example;

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
}
