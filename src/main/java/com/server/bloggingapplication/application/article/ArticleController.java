package com.server.bloggingapplication.application.article;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.server.bloggingapplication.domain.article.Article;
import com.server.bloggingapplication.domain.article.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogapp/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/")
    public ResponseEntity<Article> publishArticle(@RequestBody PostArticleRequest articleRequest) {

        Optional<Article> optionalOfCreatedArticle = articleService.createArticle(articleRequest);
        if (optionalOfCreatedArticle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(optionalOfCreatedArticle.get());
    }

    @PutMapping("/")
    public ResponseEntity<Article> updateArticle(@RequestBody UpdateArticleRequest articleRequest) {

        Optional<Article> optionalOfUpdatedArticle = articleService.updateArticle(articleRequest);
        if (optionalOfUpdatedArticle.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalOfUpdatedArticle.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

    @GetMapping("")
    public ResponseEntity<List<Article>> getAllRecentArticles() {

        List<Article> latestArticles = articleService.getRecentGlobalArticles();
        if (latestArticles == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(latestArticles);
    }

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<CommentResponse> postComment(@Valid @RequestBody CreateCommentRequest comment,
            @PathVariable("articleId") Integer articleId) {

        Optional<CommentResponse> generatedCommentResponse = articleService.createCommentOnArticle(articleId, comment);
        if (!generatedCommentResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedCommentResponse.get());
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForAnArticle(@PathVariable("articleId") Integer articleId) {

        Optional<List<CommentResponse>> optionalOfCommentsList = articleService.getCommentsForArticle(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(optionalOfCommentsList.orElse(null));
    }
}
