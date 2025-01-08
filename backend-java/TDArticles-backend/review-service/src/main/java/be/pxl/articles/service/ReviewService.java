package be.pxl.articles.service;

import be.pxl.articles.controller.request.ReviewRequest;
import be.pxl.articles.controller.response.ReviewResponse;
import be.pxl.articles.domain.Review;
import be.pxl.articles.exception.ReviewNotFoundException;
import be.pxl.articles.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewResponse getReview(Long articleId) {
        return mapToReviewResponse(reviewRepository.findByArticleId(articleId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with article id " + articleId + " not found")));
    }

    public Long postReview(Long articleId, ReviewRequest reviewRequest) {
        Review review = mapToEntity(articleId, reviewRequest);
        return reviewRepository.save(review).getId();
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .articleId(review.getArticleId())
                .approved(review.isApproved())
                .build();
    }

    private Review mapToEntity(Long articleId, ReviewRequest reviewRequest) {
        return Review.builder()
                .articleId(articleId)
                .approved(reviewRequest.isApproved())
                .build();
    }
}
