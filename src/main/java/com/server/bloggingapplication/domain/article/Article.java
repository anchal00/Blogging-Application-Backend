package com.server.bloggingapplication.domain.article;

import java.util.Date;

public class Article {

    private String title;
    private String author;
    private String description;
    private String articleBody;
    private Date createdAt;
    private Date updatedAt;

    public Article(String title, String author, String description, String articleBody, Date createdAt,
            Date updatedAt) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

}
