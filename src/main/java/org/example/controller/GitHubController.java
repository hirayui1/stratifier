package org.example.controller;

import org.example.entity.GitHubUser;
import org.example.entity.Organization;
import org.example.entity.Repo;
import org.example.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/github")
public class GitHubController {
    private final GitHubService gitHubService;
    Organization organization;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    // returns to the given map
    // TODO: duplicate null checks, could make a method out of it
    // TODO: second time querying doesn't give the correct id (because of null check, find a better way)
    // TODO: handle the error of path not existing
    @GetMapping("/orgs/{org}")
    public String getGitHubOrg(@PathVariable String org) {
        // trying to not fetch data every time - (make-shift caching?) - maybe deque with a list of 10 to keep the last 10 orgs in "cache"
        if (organization == null) {
            organization = gitHubService.getGitHubOrgProfile(org);
        }
        return organization.getLogin();
    }

    @GetMapping("/repos/{owner}/{repo}/contributors")
    public GitHubUser[] getContributorsByRepo(@PathVariable String owner, @PathVariable String repo) {
        return gitHubService.getRepoContributors(owner, repo);
    }

    @GetMapping("/orgs/{org}/repos")
    public List<String> getOrgRepos(@PathVariable String org) {
        Repo[] repos = gitHubService.getGitHubOrgRepos(org);
        return Arrays.stream(repos).map(Repo::getFull_name).collect(Collectors.toList());
    }
}
