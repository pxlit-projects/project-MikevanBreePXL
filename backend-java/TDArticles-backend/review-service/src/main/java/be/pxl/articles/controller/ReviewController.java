package be.pxl.articles.controller;

import be.pxl.articles.controller.request.ReviewRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.controller.response.ReviewResponse;
import be.pxl.articles.service.IReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @GetMapping
    public List<ArticleResponse> getPendingReviews(@NotNull @RequestHeader("Username") String username) {
        return reviewService.getPendingArticles(username);
    }

    @GetMapping("/{articleId}")
    public ReviewResponse getReview(@PathVariable Long articleId, @NotNull @RequestHeader("Username") String username) {
        return reviewService.getReviewByArticleId(articleId);
    }

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> postReview(@PathVariable Long articleId, @Valid @RequestBody ReviewRequest reviewRequest, @NotNull @RequestHeader("Username") String username) {
        long createdReviewId = reviewService.postReview(articleId, reviewRequest, username);
        return ResponseEntity.created(URI.create("/review/" + createdReviewId)).build();
    }
}