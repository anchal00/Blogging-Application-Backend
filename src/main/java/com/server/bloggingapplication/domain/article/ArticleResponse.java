package com.server.bloggingapplication.domain.article;

import java.util.Set;

public class ArticleResponse {
    
    private Article article;
    private Set<String> tags;

    public ArticleResponse(Article article, Set<String> tags) {
        this.article = article;
        this.tags = tags;
    }

    public Article getArticle() {
        return article;
    }

    public Set<String> getTags() {
        return tags;
    }
}
