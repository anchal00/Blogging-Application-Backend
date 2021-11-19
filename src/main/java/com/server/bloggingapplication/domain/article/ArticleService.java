package com.server.bloggingapplication.domain.article;

import java.util.Optional;

import com.server.bloggingapplication.application.article.PostArticleRequest;

public interface ArticleService {

    public Optional<Article> createArticle(PostArticleRequest createArticleRequest);
    public Optional<Article> getArticleBySlug(String slug);

}
