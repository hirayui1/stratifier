package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/orgs/{org}/id")
    public int getGitHubOrgId(@PathVariable String org) {
        if (organization == null) {
            getGitHubOrg(org);
        }
        return organization.getId();
    }
}
