package org.example.controller;

import org.example.entity.GitHubUser;
import org.example.entity.Repo;
import org.example.service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/github")
public class GitHubController {
    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    // returns to the requested path
    @GetMapping("/org/{owner}/contributors")
    public List<GitHubUser> getContributorsByRepo(@PathVariable String owner) {
        List<Repo> repoList = getOrgRepos(owner);

        // concurrent GET request to api.github for each repo the org owns
        List<GitHubUser> contributorsList = repoList
                .parallelStream()
                .flatMap(repo -> {
                    ArrayList<GitHubUser> contributors = new ArrayList<>(); // creating a list to hold all the contributors from every repository

                    int page = 0;
                    String formattedRepoName = repo.getFull_name().substring(repo.getFull_name().indexOf("/") + 1); // extracting repo name from full name

                    while (true) {
                        GitHubUser[] contributorPerPage = gitHubService.getRepoContributors(owner, formattedRepoName, page); // fetching contributors per repo per page

                        // break conditions
                        if (contributorPerPage == null) {
                            break;
                        } else if (contributorPerPage.length != 100) {
                            contributors.addAll(Arrays.asList(contributorPerPage));
                            break;
                        } else {
                            contributors.addAll(Arrays.asList(contributorPerPage));
                            page++;
                        }
                    }
                    return contributors.stream();
                }).collect(Collectors.toList());

        Map<String, GitHubUser> contributorMap = new ConcurrentHashMap<>();

        // weeding out the duplicates and summing duplicate values concurrently
        contributorsList.parallelStream()
                        .forEach(user -> {
                            contributorMap
                                    .computeIfAbsent(user.getLogin(), login -> user)
                                    .sumCont(user.getContributions());
                        });

        contributorsList = new ArrayList<>(contributorMap.values());

        contributorsList.sort(Comparator.comparingInt(GitHubUser::getContributions).reversed());

        return contributorsList;
    }

    public List<Repo> getOrgRepos(String org) {
        ArrayList<Repo> repoList = new ArrayList<>();
        int page = 0;

        while (true) {
            Repo[] repos = gitHubService.getGitHubOrgRepos(org, page); // fetching repos per page

            // break conditions
            if (repos == null ) {
                break;
            } else if (repos.length != 100) {
                repoList.addAll(Arrays.asList(repos));
                break;
            } else {
                repoList.addAll(Arrays.asList(repos));
                page++;
            }
        }

        return repoList;
    }
}