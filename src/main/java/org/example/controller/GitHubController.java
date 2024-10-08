package org.example.controller;

import org.example.entity.GitHubUser;
import org.example.entity.Repo;
import org.example.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequestMapping("/github")
public class GitHubController {
    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }


    @GetMapping("/org/{owner}")
    public String checkReposResponse(@PathVariable String owner) {
        return gitHubService.getGitHubOrgRepos2(owner);
    }

    // returns to the requested path
    @GetMapping("/org/{owner}/contributors")
    public List<GitHubUser> getContributorsByRepo(@PathVariable String owner) {

        Instant start = Instant.now();
        List<Repo> repoList = getOrgRepos(owner);

        // concurrent GET request to api.github for each repo the org owns
        List<GitHubUser> contributorsList = repoList
                .parallelStream()
                .flatMap(repo -> {
                    ArrayList<GitHubUser> contributors = new ArrayList<>(); // creating a list to hold all the contributors from every repository

                    int page = 0;
                    String formattedRepoName = repo.getFull_name().substring(repo.getFull_name().indexOf("/") + 1); // extracting repo name from full name

                    while (true) {
                        GitHubUser[] contributorPerPage;
                        try {
                            contributorPerPage = gitHubService.getRepoContributors(owner, formattedRepoName, page); // fetching contributors per repo per page
                        } catch (RestClientException e) {
                            System.out.format("%s's %s had unfitting json format at page %s.", owner, formattedRepoName, page);
                            continue;
                        }

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
                        .forEach(user -> contributorMap
                                .computeIfAbsent(user.getLogin(), login -> user)
                                .sumCont(user.getContributions()));

        contributorsList = new ArrayList<>(contributorMap.values());

        contributorsList.sort(Comparator.comparingInt(GitHubUser::getContributions).reversed());
        // measuring time here
        measureFinishAndPrint(start);

        return contributorsList;
    }

    public List<Repo> getOrgRepos(String org) {
        ArrayList<Repo> repoList = new ArrayList<>();
        int page = 0;

        while (true) {
            Repo[] repos;
            try {
                repos = gitHubService.getGitHubOrgRepos(org, page); // fetching repos per page

            } catch (RestClientException e) {
                System.err.println(Arrays.toString(gitHubService.getGitHubOrgRepos(org, page)));
                break;
            }

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
        System.out.println(repoList.size());
        return repoList;
    }

    @GetMapping("/repos/{org}")
    public List<Repo> getOrgRepos2(@PathVariable String org) {

        return IntStream.iterate(0, page -> page + 1)  // Start from page 0
                .parallel()
                .mapToObj(page -> gitHubService.getGitHubOrgRepos(org, page))  // Fetch each page of repos
                .takeWhile(array -> array != null && array.length > 0) // Stop when an empty page is found
                .flatMap(Arrays::stream)  // Flatten each List<Repo> into a Stream<Repo>
                .collect(Collectors.toList());
    }

    public void measureFinishAndPrint(Instant start) {
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        long seconds = duration.getSeconds();
        long milliseconds = duration.toMillis();

        System.out.println("Total Time: " + seconds + " seconds and " + (milliseconds % 1000) + " milliseconds");
    }
}