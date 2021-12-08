package com.server.bloggingapplication.application.article;

public class CommentResponse {

    private Integer commentId;
    private Integer authorId;
    private Integer articleId;
    private String author;
    private String body;
    private String createdAt;

    public CommentResponse(Integer commentId, Integer authorId, Integer articleId, String author, String body,
            String createdAt) {
        this.commentId = commentId;
        this.articleId = articleId;
        this.authorId = authorId;
        this.author = author;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getAuthorId() {
        return authorId;
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
