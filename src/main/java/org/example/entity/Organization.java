package org.example.entity;

import java.util.HashMap;
import java.util.List;

public class Organization {
    private String login;
    private List[] repos;
    private String[] repos_url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List[] getRepos() {
        return repos;
    }

    public void setRepos(List[] repos) {
        this.repos = repos;
    }

    public String[] getRepos_url() {
        return repos_url;
    }

    public void setRepos_url(String[] repos_url) {
        this.repos_url = repos_url;
    }
}
