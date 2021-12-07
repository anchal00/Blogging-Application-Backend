package com.server.bloggingapplication.domain.article;

import java.util.List;

import com.server.bloggingapplication.application.article.CommentResponse;
import com.server.bloggingapplication.application.article.CreateCommentRequest;
import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;

public interface ArticleDAO {

    public Article createArticle(Integer userId, PostArticleRequest articleRequest);
    public List<Article> fetchLatestArticles();
    public Article updateArticle(Integer userId, UpdateArticleRequest articleRequest);
    public CommentResponse createCommentOnArticle(Integer articleId, Integer publisherId ,String publisherName,CreateCommentRequest commentRequest);
    
    
}
