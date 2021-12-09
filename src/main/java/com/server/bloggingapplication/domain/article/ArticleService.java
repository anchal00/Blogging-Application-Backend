package com.server.bloggingapplication.domain.article;

import java.util.List;
import java.util.Optional;

import com.server.bloggingapplication.application.article.CommentResponse;
import com.server.bloggingapplication.application.article.CreateCommentRequest;
import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;

public interface ArticleService {

    public Optional<ArticleResponse> createArticle(PostArticleRequest createArticleRequest);
    public Optional<Article> updateArticle(UpdateArticleRequest articleRequest);
    public List<ArticleResponse> getRecentGlobalArticles();
    public Optional<CommentResponse> createCommentOnArticle(Integer articleId, CreateCommentRequest commentRequest);
    public Optional<List<CommentResponse>> getCommentsForArticle(Integer articleId);
    public boolean favouriteArticle(Integer articleId);
    public boolean UnfavouriteArticle(Integer articleId);
    public List<ArticleResponse> getHomeFeedArticles();
    public List<ArticleResponse> getArticlesWithTag(String tag);
    public List<ArticleResponse> getArticlesFromAuthor(String authorUserName);
}
