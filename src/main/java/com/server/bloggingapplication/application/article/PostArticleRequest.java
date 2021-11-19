package com.server.bloggingapplication.application.article;

import java.util.List;

public class PostArticleRequest {

    //@NotBlank
    private String title;
    //@NotBlank
    private String description;
    //@NotBlank
    private String body;
    private List<String> tags;

    PostArticleRequest() {
    }

    public PostArticleRequest(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public PostArticleRequest(String title, String description, String body, List<String> tags) {
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

    public List<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }
}
