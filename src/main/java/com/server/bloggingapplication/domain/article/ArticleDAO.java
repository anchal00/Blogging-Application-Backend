package com.server.bloggingapplication.domain.article;

import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;

public interface ArticleDAO {

    public Article createArticle(Integer userId, PostArticleRequest articleRequest);

    public Article updateArticle(Integer userId, UpdateArticleRequest articleRequest);
    
    
}
