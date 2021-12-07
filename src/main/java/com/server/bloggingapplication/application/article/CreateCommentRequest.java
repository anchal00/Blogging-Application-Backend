package com.server.bloggingapplication.application.article;

import javax.validation.constraints.NotBlank;

public class CreateCommentRequest {

    @NotBlank
    private String body;

    public CreateCommentRequest(){}
    
    public CreateCommentRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
