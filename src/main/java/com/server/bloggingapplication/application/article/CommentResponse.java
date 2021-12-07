package com.server.bloggingapplication.application.article;

public class CommentResponse {

    private Integer commentId;
    private String authorOfComment;
    private String body;
    private String createdAt;

    public CommentResponse(Integer commentId, String authorOfComment, String body, String createdAt) {
        this.commentId = commentId;
        this.authorOfComment = authorOfComment;
        this.body = body;
        this.createdAt = createdAt;
    }

    public String getAuthorOfComment() {
        return authorOfComment;
    }

    public String getBody() {
        return body;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    
}
