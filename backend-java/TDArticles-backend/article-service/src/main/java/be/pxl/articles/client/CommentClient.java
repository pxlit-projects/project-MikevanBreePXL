package be.pxl.articles.client;

import be.pxl.articles.controller.response.CommentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "comment-service")
public interface CommentClient {

    @GetMapping("/article/{articleId}")
    @CircuitBreaker(name = "comments-list-circuit-breaker", fallbackMethod = "getAllCommentsFromArticleFallback")
    List<CommentResponse> getAllCommentsFromArticle(@PathVariable Long articleId, @RequestHeader("Username") String username);

    default List<CommentResponse> getAllCommentsFromArticleFallback(Throwable throwable) {
        return List.of();
    }
}