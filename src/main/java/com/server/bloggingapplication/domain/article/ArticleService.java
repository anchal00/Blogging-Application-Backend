package com.server.bloggingapplication.domain.article;

import java.util.List;
import java.util.Optional;

import com.server.bloggingapplication.application.article.CommentResponse;
import com.server.bloggingapplication.application.article.CreateCommentRequest;
import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;

public interface ArticleService {

    public Optional<Article> createArticle(PostArticleRequest createArticleRequest);
    public Optional<Article> updateArticle(UpdateArticleRequest articleRequest);
    public List<Article> getRecentGlobalArticles();
    public Optional<CommentResponse> createCommentOnArticle(Integer articleId, CreateCommentRequest commentRequest);
}
