package com.toozy.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String login;
    private Long id;
    private String avatarUrl;

    public String getName() {
        return login;
    }
}