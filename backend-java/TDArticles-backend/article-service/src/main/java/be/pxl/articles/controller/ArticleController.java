package be.pxl.articles.controller;

import be.pxl.articles.client.CommentClient;
import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.request.EditArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.service.IArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ArticleController {
    private final IArticleService articleService;
    private final CommentClient commentClient;

    @ModelAttribute
    public void addAttributes(@RequestHeader("Username") String username) {
        // You can store the username in a field or use it as needed
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getPublishedArticles(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String content
    ) {
        List<ArticleResponse> articles = articleService.getPublishedArticles(from, to, author, content);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/pending")
    public List<ArticleResponse> getPendingArticles() {
        return articleService.getPendingArticles();
    }

    @GetMapping("/publish-ready")
    public List<ArticleResponse> getReadyToPublishArticles(@RequestHeader("Username") String author) {
        return articleService.getReadyToPublishArticles(author);
    }

    @GetMapping("/review")
    public List<ArticleResponse> getRejectedArticles(@RequestHeader("Username") String author) {
        return articleService.getRejectedArticles(author);
    }

    @GetMapping("/concepts")
    public List<ArticleResponse> getConceptArticlesByAuthor(@RequestHeader("Username") String author) {
        return articleService.getConcepts(author);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable long id, @RequestHeader("Username") String username) {
        ArticleResponse article = articleService.getArticle(id);
        article.setComments(commentClient.getAllCommentsFromArticle(id, username));

        return ResponseEntity.ok(article);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createArticle(@Valid @RequestBody CreateArticleRequest request) {
        long createdArticleId = articleService.createArticle(request);
        return ResponseEntity.created(URI.create("/article/" + createdArticleId)).build();
    }

    @PostMapping("{id}/publish/review")
    public ResponseEntity<Void> publishReview(@PathVariable long id, @RequestBody Boolean approved) {
        articleService.publishReview(id, approved);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/publish/article")
    public ResponseEntity<Void> publishArticle(@PathVariable long id, @RequestBody Boolean publish, @RequestHeader("Username") String username) {
        articleService.publishArticle(id, publish, username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editArticle(@PathVariable long id, @Valid @RequestBody EditArticleRequest request) {
        articleService.editArticle(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id, @RequestHeader("Username") String username) {
        articleService.deleteArticle(id, username);
        return ResponseEntity.noContent().build();
    }
}
