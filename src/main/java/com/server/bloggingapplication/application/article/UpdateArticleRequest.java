package com.server.bloggingapplication.application.article;

import javax.validation.constraints.NotBlank;

public class UpdateArticleRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String body;

    public UpdateArticleRequest(@NotBlank String title, @NotBlank String description, @NotBlank String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
