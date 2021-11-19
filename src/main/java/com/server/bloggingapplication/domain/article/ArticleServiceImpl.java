package com.server.bloggingapplication.domain.article;

import java.security.Principal;
import java.util.Optional;

import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.domain.user.User;
import com.server.bloggingapplication.domain.user.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getPrincipal().toString();
        Optional<User> optionalOfUser = userDAO.findByUserName(userName);

        if (!optionalOfUser.isPresent()) {
            return Optional.empty();
        }
        Integer userId = optionalOfUser.get().getId();

        Article createdArticle = articleDAO.createArticle(userId, createArticleRequest);

        return Optional.of(createdArticle);
    }

    @Override
    public Optional<Article> getArticleBySlug(String slug) {

        return null;
    }

}
