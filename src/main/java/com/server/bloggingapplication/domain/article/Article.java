package com.server.bloggingapplication.domain.article;

import java.sql.Timestamp;

public class Article {

    private String title;
    private String author;
    private String description;
    private String articleBody;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Article(String title, String author, String description, String articleBody, Timestamp createdAt,
            Timestamp updatedAt) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.articleBody = articleBody;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

}
