package com.server.bloggingapplication.domain.article;

import com.server.bloggingapplication.application.article.PostArticleRequest;

public interface ArticleDAO {

    public Article createArticle(Integer userId, PostArticleRequest articleRequest);
    
    
}
