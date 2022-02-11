package com.server.bloggingapplication.domain.article;

import java.util.List;
import java.util.Optional;

import com.server.bloggingapplication.application.article.CommentResponse;
import com.server.bloggingapplication.application.article.CreateCommentRequest;
import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;
import com.server.bloggingapplication.domain.user.User;
import com.server.bloggingapplication.domain.user.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ArticleDAO articleDAO;

    @Override
    public Optional<ArticleResponse> createArticle(@RequestBody PostArticleRequest createArticleRequest) {
        String userName = getCurrentUserInfo();

        Optional<User> optionalOfUser = userDAO.findByUserName(userName);

        if (!optionalOfUser.isPresent()) {
            return Optional.empty();
        }

        Integer userId = optionalOfUser.get().getId();

        ArticleResponse createdArticle = articleDAO.createArticle(userId, createArticleRequest);

        return Optional.of(createdArticle);
    }

    private String getCurrentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated())
            return null;
        String userName = auth.getPrincipal().toString();
        return userName;
    }

    @Override
    public Optional<Article> updateArticle(UpdateArticleRequest articleRequest) {

        String currentUserName = getCurrentUserInfo();
        if (currentUserName == null) return Optional.empty();
        Article updatedArticle = articleDAO.updateArticle(articleRequest,currentUserName);
        if (updatedArticle == null) {
            return Optional.empty();
        }
        return Optional.of(updatedArticle);
    }

    @Override
    public List<ArticleResponse> getRecentGlobalArticles() {
        return articleDAO.fetchLatestArticles();
    }

    @Override
    public Optional<CommentResponse> createCommentOnArticle(String articleTitle, CreateCommentRequest commentRequest) {

        String userName = getCurrentUserInfo();
        if (userName == null || userName.equals("anonymousUser")) {
            return Optional.empty();
        }
        Optional<User> optionalOfUser = userDAO.findByUserName(userName);

        CommentResponse createdComment = articleDAO.createCommentOnArticle(articleTitle, optionalOfUser.get().getId(),
                optionalOfUser.get().getFirstName() + " " + optionalOfUser.get().getLastName(), commentRequest);

        if (createdComment == null) {
            return Optional.empty();
        }
        return Optional.of(createdComment);
    }

    @Override
    public Optional<List<CommentResponse>> getCommentsForArticle(String articleTitle) {

        List<CommentResponse> comments = articleDAO.fetchAllCommentsForArticle(articleTitle);

        return Optional.of(comments);
    }

    @Override
    public boolean favouriteArticle(String articleTitle) {

        String userName = getCurrentUserInfo();
        if (userName == null) {
            return false;
        }

        boolean markedFavourite = articleDAO.markArticleAsFavouriteForUser(articleTitle, userName);

        return markedFavourite;
    }

    @Override
    public boolean UnfavouriteArticle(String articleTitle) {

        String userName = getCurrentUserInfo();
        if (userName == null) {
            return false;
        }

        boolean markedUnFavourite = articleDAO.markArticleAsUnFavouriteForUser(articleTitle, userName);

        return markedUnFavourite;
    }

    @Override
    public List<ArticleResponse> getHomeFeedArticles() {

        String userName = getCurrentUserInfo();
        if (userName == null) {
            return null;
        }
        List<ArticleResponse> articles = articleDAO.fetchArticlesFromFollowedUsers(userName);

        return articles;
    }

    @Override
    public List<ArticleResponse> getArticlesWithTag(String tag) {

        List<ArticleResponse> articles = articleDAO.fetchArticlesByTag(tag);
        return articles;
    }

    @Override
    public List<ArticleResponse> getArticlesFromAuthor(String authorUserName) {
        List<ArticleResponse> articles = articleDAO.fetchArticlesByAuthor(authorUserName);
        return articles;
    }

    @Override
    public List<ArticleResponse> getArticlesFavouritedByUser(String userName) {
        List<ArticleResponse> articles = articleDAO.fetchArticlesFavouritedByUser(userName);
        return articles;
    }

    @Override
    public boolean deleteArticle(String articleTitle) {

        String currentUser = getCurrentUserInfo();
        if (currentUser == null) {
            return false;
        }
        boolean deleted = articleDAO.deleteArticleByTitle(articleTitle, currentUser);
        return deleted;
    }

    @Override
    public Optional<ArticleResponse> getArticleByTitle(String articleTitle) {

        ArticleResponse articleResponse = articleDAO.fetchArticlesByTitle(articleTitle);
        if (articleResponse == null) {
            return Optional.empty();
        }
        return Optional.of(articleResponse);
    }

    @Override
    public boolean deleteCommentFromArticle(String articleTitle, Integer commentId) {
        String currentUser = getCurrentUserInfo();
        if (currentUser == null) {
            return false;
        }
        Optional<User> currentUserOptional = userDAO.findByUserName(currentUser);

        if (!currentUserOptional.isPresent()) {
            return false;
        }

        boolean isDeleted = articleDAO.deleteCommentFromArticleByUser(currentUserOptional.get().getId(),
                articleTitle, commentId);

        return isDeleted;
    }
}
