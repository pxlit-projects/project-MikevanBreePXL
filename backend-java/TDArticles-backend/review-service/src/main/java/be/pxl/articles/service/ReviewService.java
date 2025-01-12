package be.pxl.articles.service;

import be.pxl.articles.client.ArticleClient;
import be.pxl.articles.controller.request.ReviewRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.controller.request.NotificationRequest;
import be.pxl.articles.controller.response.ReviewResponse;
import be.pxl.articles.domain.Review;
import be.pxl.articles.exception.ReviewNotFoundException;
import be.pxl.articles.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ArticleClient articleClient;
    private final RabbitTemplate rabbitTemplate;

    public List<ArticleResponse> getPendingArticles(String username) {
        return articleClient.getPendingArticles(username)
                .stream()
                .toList();
    }

    public ReviewResponse getReviewByArticleId(Long articleId) {
        return mapToReviewResponse(reviewRepository.findAllByArticleId(articleId).stream().findFirst()
                .orElseThrow(() -> new ReviewNotFoundException("Review with article id " + articleId + " not found")));
    }

    public Long postReview(Long articleId, ReviewRequest reviewRequest, String username) {
        Review review = mapToEntity(articleId, reviewRequest);

        articleClient.publishReview(articleId, username, reviewRequest.isApproved());
        sendNotification(articleId, reviewRequest, username);

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
        var reviewBuilder = Review.builder()
                .articleId(articleId)
                .approved(reviewRequest.isApproved());
        if (!reviewRequest.isApproved()) {
            reviewBuilder.rejectionNotes(reviewRequest.getRejectionNotes());
        }
        return reviewBuilder.build();
    }

    private void sendNotification(Long articleId, ReviewRequest reviewRequest, String reviewer) {
        var notificationRequest = NotificationRequest.builder()
                .sender(reviewer)
                .receiver(reviewRequest.getReceiver());

        StringBuilder message = new StringBuilder("Review ");
        if (reviewRequest.isApproved()) {
            message.append("approved");
        } else {
            message.append("rejected");
        }
        message.append(" for article #").append(articleId);
        if (!reviewRequest.isApproved()) {
            message.append("\n\nNotes:\n").append(reviewRequest.getRejectionNotes());
        }

        notificationRequest.message(message.toString());

        rabbitTemplate.convertAndSend("NotificationsQueue", notificationRequest.build());
    }
}
