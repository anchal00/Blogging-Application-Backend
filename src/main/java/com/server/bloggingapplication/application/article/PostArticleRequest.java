package com.server.bloggingapplication.application.article;

import java.util.Set;

import javax.validation.constraints.NotBlank;

public class PostArticleRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String body;
    private Set<String> tags;

    PostArticleRequest() {
    }

    public PostArticleRequest(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public PostArticleRequest(String title, String description, String body, Set<String> tags) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tags = tags;
    }

    public String getBody() {
        return body;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }
}
