package org.example.entity;

import java.util.HashMap;

public class Organization {
    private String login;
    private Repo[] repos;
    private String[] repos_url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Repo[] getRepos() {
        return repos;
    }

    public void setRepos(Repo[] repos) {
        this.repos = repos;
    }

    public String[] getRepos_url() {
        return repos_url;
    }

    public void setRepos_url(String[] repos_url) {
        this.repos_url = repos_url;
    }
}
