package be.pxl.articles.service;

import be.pxl.articles.controller.request.ReviewRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.controller.response.ReviewResponse;

import java.util.List;

public interface IReviewService {
    List<ArticleResponse> getPendingArticles(String username);

    ReviewResponse getReviewByArticleId(Long articleId);

    Long postReview(Long articleId, ReviewRequest reviewRequest, String username);

    void sendNotification(Long articleId, ReviewRequest reviewRequest, String reviewer);
}
