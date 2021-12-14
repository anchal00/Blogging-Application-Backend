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

    public Optional<CommentResponse> createCommentOnArticle(String articleTitle, CreateCommentRequest commentRequest);

    public Optional<List<CommentResponse>> getCommentsForArticle(String articleTitle);

    public boolean favouriteArticle(String articleTitle);

    public boolean UnfavouriteArticle(String articleTitle);

    public List<ArticleResponse> getHomeFeedArticles();

    public List<ArticleResponse> getArticlesWithTag(String tag);

    public List<ArticleResponse> getArticlesFromAuthor(String authorUserName);

    public List<ArticleResponse> getArticlesFavouritedByUser(String userName);

    public boolean deleteArticle(String articleTitle);

    public Optional<ArticleResponse> getArticleByTitle(String articleTitle);

    public boolean deleteCommentFromArticle(String articleTitle, Integer commentId);
}
