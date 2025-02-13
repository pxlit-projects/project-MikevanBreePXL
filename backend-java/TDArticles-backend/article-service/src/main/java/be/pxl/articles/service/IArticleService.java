package be.pxl.articles.service;

import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.request.EditArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IArticleService {
    List<ArticleResponse> getPublishedArticles(LocalDateTime from, LocalDateTime to, String author, String content);
    ArticleResponse getArticle(long id);
    long createArticle(CreateArticleRequest request);
    void editArticle(long id, EditArticleRequest request);
    List<ArticleResponse> getConcepts(String author);
    List<ArticleResponse> getPendingArticles();
    List<ArticleResponse> getReadyToPublishArticles(String author);
    List<ArticleResponse> getRejectedArticles(String author);
    void publishReview(long id, boolean approved);
    void publishArticle(long id, boolean published, String username);
    void deleteArticle(long id, String username);
}
