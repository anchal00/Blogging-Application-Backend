package com.server.bloggingapplication.domain.article;

import java.util.List;

import com.server.bloggingapplication.application.article.CommentResponse;
import com.server.bloggingapplication.application.article.CreateCommentRequest;
import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;

public interface ArticleDAO {

    public ArticleResponse createArticle(Integer userId, PostArticleRequest articleRequest);

    public List<ArticleResponse> fetchLatestArticles();

    public Article updateArticle(UpdateArticleRequest articleRequest);

    public CommentResponse createCommentOnArticle(String articleTitle, Integer publisherId, String publisherName,
            CreateCommentRequest commentRequest);

    public List<CommentResponse> fetchAllCommentsForArticle(String articleTitle);

    public boolean markArticleAsFavouriteForUser(String articleTitle, String userName);

    public boolean markArticleAsUnFavouriteForUser(String articleTitle, String userName);

    public List<ArticleResponse> fetchArticlesFromFollowedUsers(String userName);

    public List<ArticleResponse> fetchArticlesByTag(String tag);

    public List<ArticleResponse> fetchArticlesByAuthor(String authorUserName);

    public List<ArticleResponse> fetchArticlesFavouritedByUser(String userName);

    public boolean deleteArticleById(String articleTitle);

}
