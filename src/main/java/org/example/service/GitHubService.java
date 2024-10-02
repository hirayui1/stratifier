package org.example.service;

import org.example.entity.GitHubUser;
import org.example.entity.Repo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@Service
public class GitHubService {
    private final RestTemplate restTemplate;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // loads the data from API
    public Repo[] getGitHubOrgRepos(String org, int page) {
        String url = String.format("https://api.github.com/orgs/%s/repos?per_page=100&page=%s", org, page);
        Repo[] repos = null;
        try {
            repos = restTemplate.getForObject(url, Repo[].class);
            return repos;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) { // if 403 - GitHub API sometimes fails if the repository is too big
                System.err.println("403 Forbidden for URL: " + url + ". Array state at error: " + Arrays.toString(repos) + "\nSkipping...");
                return null;
            }
            throw e;
        } catch (RestClientException e) {
            System.err.println("getGitHubOrgRepos: 403 Forbidden for URL: " + url + ". Array state at error: " + Arrays.toString(repos) + "\nSkipping...");
            return null;
        }
    }

    public GitHubUser[] getRepoContributors(String owner, String repo, int page) {
        String url = String.format("https://api.github.com/repos/%s/%s/contributors?per_page=100&page=%s", owner, repo, page);
        GitHubUser[] users = null;
        try {
            users = restTemplate.getForObject(url, GitHubUser[].class);
            return users;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) { // if 403 - GitHub API sometimes fails if the repository is too big
                System.err.println("403 Forbidden for URL: " + url + ". Array state at error: " + Arrays.toString(users) + "\nSkipping...");
                return null;
            }
            throw e;
        } catch (RestClientException e) {
            System.err.println("getRepoContributors: 403 Forbidden for URL: " + url + ". Array state at error: " + Arrays.toString(users) + "\nSkipping...");
            return null;
        }
    }

    public String getGitHubOrgRepos2(String owner) {
        String url = String.format("https://api.github.com/orgs/%s/repos?per_page=100&page=0", owner);
        return restTemplate.getForObject(url, String.class);
    }
}