package com.server.bloggingapplication.domain.article;

public class Article {

    private String title;
    private String author;
    private String description;
    private String articleBody;
    private String createdAt;
    private String updatedAt;

    public Article(String title, String author, String description, String articleBody, String createdAt,
            String updatedAt) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
