package org.example.entity;

import java.util.HashMap;

public class Repo {
    private String name;
    private String full_name;
    private HashMap<String, GitHubUser> contributors;

    public Repo() {

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}