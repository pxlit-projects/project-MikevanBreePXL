package be.pxl.articles.client;

import be.pxl.articles.controller.response.ArticleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "article-service")
public interface ArticleClient {

    @GetMapping("/pending")
    List<ArticleResponse> getPendingArticles();

    @PostMapping("{id}/publish")
    ResponseEntity<Void> publishArticle(@PathVariable long id, @RequestBody Boolean approved);
}