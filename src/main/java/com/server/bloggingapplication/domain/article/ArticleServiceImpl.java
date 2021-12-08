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
    public Optional<Article> createArticle(@RequestBody PostArticleRequest createArticleRequest) {
        String userName = getCurrentUserInfo();

        Optional<User> optionalOfUser = userDAO.findByUserName(userName);

        if (!optionalOfUser.isPresent()) {
            return Optional.empty();
        }

        Integer userId = optionalOfUser.get().getId();

        Article createdArticle = articleDAO.createArticle(userId, createArticleRequest);

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

        String userName = getCurrentUserInfo();
        if (userName == null)
            return Optional.empty();

        Optional<User> optionalOfUser = userDAO.findByUserName(userName);

        if (!optionalOfUser.isPresent()) {
            return Optional.empty();
        }

        Integer userId = optionalOfUser.get().getId();

        Article updatedArticle = articleDAO.updateArticle(userId, articleRequest);
        if (updatedArticle == null) {
            return Optional.empty();
        }
        return Optional.of(updatedArticle);
    }

    @Override
    public List<Article> getRecentGlobalArticles() {
        return articleDAO.fetchLatestArticles();
    }

    @Override
    public Optional<CommentResponse> createCommentOnArticle(Integer articleId, CreateCommentRequest commentRequest) {

        String userName = getCurrentUserInfo();
        if (userName == null) {
            return Optional.empty();
        }
        Optional<User> optionalOfUser = userDAO.findByUserName(userName);

        CommentResponse createdComment = articleDAO.createCommentOnArticle(articleId, optionalOfUser.get().getId(),
                optionalOfUser.get().getFirstName() + " " + optionalOfUser.get().getLastName(), commentRequest);
                
        if (createdComment == null) {
            return Optional.empty();
        }
        return Optional.of(createdComment);
    }

    @Override
    public Optional<List<CommentResponse>> getCommentsForArticle(Integer articleId) {
        
        List<CommentResponse> comments =  articleDAO.fetchAllCommentsForArticle(articleId);
        
        return Optional.of(comments);
    }
}
