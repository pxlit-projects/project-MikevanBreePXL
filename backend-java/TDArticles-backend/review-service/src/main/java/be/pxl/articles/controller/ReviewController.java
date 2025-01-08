package be.pxl.articles.controller;

import be.pxl.articles.controller.request.ReviewRequest;
import be.pxl.articles.controller.response.ReviewResponse;
import be.pxl.articles.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{articleId}")
    public ReviewResponse getReview(@PathVariable Long articleId) {
        return reviewService.getReview(articleId);
    }

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> postReview(@PathVariable Long articleId, @Valid @RequestBody ReviewRequest reviewRequest) {
        long createdId = reviewService.postReview(articleId, reviewRequest);
        return ResponseEntity.created(URI.create("/review/" + createdId)).build();
    }
}