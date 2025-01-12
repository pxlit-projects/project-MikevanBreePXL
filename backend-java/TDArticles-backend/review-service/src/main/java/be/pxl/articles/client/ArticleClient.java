package be.pxl.articles.client;

import be.pxl.articles.controller.response.ArticleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "article-service")
public interface ArticleClient {

    @GetMapping("/pending")
    List<ArticleResponse> getPendingArticles(@RequestHeader("Username") String username);
    @PostMapping("{id}/publish/review")
    ResponseEntity<Void> publishReview(@PathVariable long id, @RequestBody Boolean approved);
}