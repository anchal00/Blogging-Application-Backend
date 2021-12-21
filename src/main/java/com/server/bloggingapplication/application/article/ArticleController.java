package com.server.bloggingapplication.application.article;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.server.bloggingapplication.domain.article.Article;
import com.server.bloggingapplication.domain.article.ArticleResponse;
import com.server.bloggingapplication.domain.article.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogapp/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/{articleTitle}")
    public ResponseEntity<ArticleResponse> getArticleByTitle(@PathVariable("articleTitle") String articleTitle) {

        Optional<ArticleResponse> optionalOfArtilce = articleService.getArticleByTitle(articleTitle);
        if (!optionalOfArtilce.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalOfArtilce.get());
    }

    @PostMapping("/")
    public ResponseEntity<ArticleResponse> publishArticle(@RequestBody PostArticleRequest articleRequest,
            @RequestHeader("Authorization") String authorization) {

        Optional<ArticleResponse> optionalOfCreatedArticle = articleService.createArticle(articleRequest);
        if (optionalOfCreatedArticle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(optionalOfCreatedArticle.get());
    }

    @DeleteMapping("/{articleTitle}")
    public ResponseEntity<String> deleteArticle(@PathVariable("articleTitle") String articleTitle,
            @RequestHeader("Authorization") String authorization) {
        boolean isDeleted = articleService.deleteArticle(articleTitle);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Article Deleted Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

    @PutMapping("/")
    public ResponseEntity<Article> updateArticle(@RequestBody UpdateArticleRequest articleRequest,
            @RequestHeader("Authorization") String authorization) {

        Optional<Article> optionalOfUpdatedArticle = articleService.updateArticle(articleRequest);
        if (optionalOfUpdatedArticle.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalOfUpdatedArticle.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

    @PostMapping("/{articleTitle}/comments")
    public ResponseEntity<CommentResponse> postComment(@Valid @RequestBody CreateCommentRequest comment,
            @PathVariable("articleTitle") String articleTitle,
            @RequestHeader("Authorization") String authorization) {

        Optional<CommentResponse> generatedCommentResponse = articleService.createCommentOnArticle(articleTitle,
                comment);
        if (!generatedCommentResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedCommentResponse.get());
    }

    /**
     * TODO : TEST the method
     */
    @DeleteMapping("/{articleTitle}/comments/{commentId}")
    public ResponseEntity<Boolean> deleteCommentsFromArticle(
            @PathVariable("articleTitle") String articleTitle,
            @PathVariable("commentId") Integer commentId,
            @RequestHeader("Authorization") String authorization) {

        boolean isDeleted = articleService.deleteCommentFromArticle(articleTitle, commentId);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isDeleted);
        }
        return ResponseEntity.status(HttpStatus.OK).body(isDeleted);
    }

    @GetMapping("/{articleTitle}/comments/")
    public ResponseEntity<List<CommentResponse>> getCommentsFromArticle(
            @PathVariable("articleTitle") String articleTitle) {

        Optional<List<CommentResponse>> comments = articleService.getCommentsForArticle(articleTitle);

        if (comments.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(comments.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/{articleTitle}/favourite")
    public ResponseEntity<Boolean> markArticleAsFavourite(@PathVariable("articleTitle") String articleTitle,
            @RequestHeader("Authorization") String authorization) {

        boolean isFavourited = articleService.favouriteArticle(articleTitle);
        if (isFavourited) {
            return ResponseEntity.status(HttpStatus.OK).body(isFavourited);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @PostMapping("/{articleTitle}/unfavourite")
    public ResponseEntity<Boolean> markArticleAsUnFavourite(@PathVariable("articleTitle") String articleTitle,
            @RequestHeader("Authorization") String authorization) {

        boolean isUnFavourited = articleService.UnfavouriteArticle(articleTitle);
        if (isUnFavourited) {
            return ResponseEntity.status(HttpStatus.OK).body(isUnFavourited);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping(params = { "tag" })
    public ResponseEntity<List<ArticleResponse>> getArticlesHavingTags(@RequestParam("tag") String tag) {

        List<ArticleResponse> allArticles = articleService.getArticlesWithTag(tag);
        if (allArticles == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(allArticles);
    }

    @GetMapping(params = { "author" })
    public ResponseEntity<List<ArticleResponse>> getArticlesFromAuthor(@RequestParam("author") String authorUserName) {

        List<ArticleResponse> allArticles = articleService.getArticlesFromAuthor(authorUserName);
        if (allArticles == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(allArticles);
    }

    @GetMapping(params = { "favourited" })
    public ResponseEntity<List<ArticleResponse>> getArticlesFavouritedByUser(
            @RequestParam("favourited") String userName) {

        List<ArticleResponse> articlesFavouritedByUser = articleService.getArticlesFavouritedByUser(userName);
        if (articlesFavouritedByUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(articlesFavouritedByUser);
    }
}
