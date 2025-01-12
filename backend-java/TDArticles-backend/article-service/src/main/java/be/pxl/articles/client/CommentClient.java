package be.pxl.articles.client;

import be.pxl.articles.controller.response.CommentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "comment-service")
public interface CommentClient {

    @GetMapping("/article/{articleId}")
    List<CommentResponse> getAllCommentsFromArticle(@PathVariable Long articleId, @RequestHeader("Username") String username);
}