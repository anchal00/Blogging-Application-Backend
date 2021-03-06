package com.server.bloggingapplication.application.home;

import java.util.List;
import java.util.Map;

import com.server.bloggingapplication.domain.article.ArticleResponse;
import com.server.bloggingapplication.domain.article.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogapp")
public class HomeController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/homefeed")
    public ResponseEntity<Map<String, List<ArticleResponse>>> getArticleFromHomeFeed(
            @RequestHeader("Authorization") String authorization) {

        List<ArticleResponse> articlesOnHomeFeed = articleService.getHomeFeedArticles();
        if (articlesOnHomeFeed == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("Articles from your followings", articlesOnHomeFeed));
    }

    @GetMapping("/")
    public ResponseEntity<List<ArticleResponse>> getAllRecentArticles() {

        List<ArticleResponse> latestArticles = articleService.getRecentGlobalArticles();
        if (latestArticles == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(latestArticles);
    }
}
