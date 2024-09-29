package org.example.controller;

import org.example.entity.GitHubUser;
import org.example.entity.Organization;
import org.example.entity.Repo;
import org.example.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/github")
public class GitHubController {
    private final GitHubService gitHubService;
    private Organization organization;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    // returns to the given map
    // TODO: duplicate null checks, could make a method out of it
    // TODO: second time querying doesn't give the correct id (because of null check, find a better way)

    @GetMapping("/org/{owner}/contributors")
    public List<GitHubUser> getContributorsByRepo(@PathVariable String owner) {
        List<Repo> repoList = getOrgRepos(owner);

        return repoList.parallelStream()
                .flatMap(repo -> {
                    ArrayList<GitHubUser> contributors = new ArrayList<>();
                    int page = 0;

                    String formattedRepoName = repo.getFull_name().substring(repo.getFull_name().indexOf("/"));

                    while (true) {
                        GitHubUser[] contributorPerPage = gitHubService.getRepoContributors(owner, formattedRepoName, page);
                        contributors.addAll(Arrays.asList(contributorPerPage));

                        if (contributorPerPage.length != 100) {
                            break;
                        }

                        page++;
                    }

                    return contributors.stream();
                }).collect(Collectors.toList());
    }


    public List<Repo> getOrgRepos(String org) {
        Repo[] repos = gitHubService.getGitHubOrgRepos(org);

        return Arrays.asList(repos);
    }
}
