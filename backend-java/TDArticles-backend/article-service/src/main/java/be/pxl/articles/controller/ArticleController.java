package be.pxl.articles.controller;

import be.pxl.articles.client.CommentClient;
import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.request.EditArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.service.IArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getPublishedArticles(
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String content
    ) {
        List<ArticleResponse> articles = articleService.getPublishedArticles(from, to, author, content);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<ArticleResponse>> getPublishedArticlesByAuthor(@PathVariable String author) {
        List<ArticleResponse> articles = articleService.getArticlesByAuthor(author);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/concepts/{author}")
    public List<ArticleResponse> getConceptArticles(@PathVariable String author) {
        return articleService.getConcepts(author);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable long id) {
        ArticleResponse article = articleService.getArticle(id);
        article.setComments(commentClient.getAllCommentsFromArticle(id));

        return ResponseEntity.ok(article);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createArticle(@Valid @RequestBody CreateArticleRequest request) {
        long createdArticleId = articleService.createArticle(request);
        return ResponseEntity.created(URI.create("/article/" + createdArticleId)).build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> editArticle(@PathVariable long id, @Valid @RequestBody EditArticleRequest request) {
        articleService.editArticle(id, request);
        return ResponseEntity.ok().build();
    }
}
