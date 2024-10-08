package org.example.dto;

import org.example.entity.GitHubUser;

import java.util.List;

public class ContributorsDTO {
    private int count;
    private List<GitHubUser> contributors;

    public ContributorsDTO(int count, List<GitHubUser> contributors) {
        this.count = count;
        this.contributors = contributors;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GitHubUser> getContributors() {
        return contributors;
    }

    public void setContributors(List<GitHubUser> contributors) {
        this.contributors = contributors;
    }
}
