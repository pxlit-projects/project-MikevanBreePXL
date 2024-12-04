package be.pxl.articles.controller;

import be.pxl.articles.domain.api.CreatePostRequest;
import be.pxl.articles.domain.api.EditPostRequest;
import be.pxl.articles.domain.api.PostResponse;
import be.pxl.articles.service.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final IPostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPublishedPosts(
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String content
    ) {
        List<PostResponse> posts = postService.getPublishedPosts(from, to, author, content);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(String author) {
        List<PostResponse> posts = postService.getPostsByAuthor(author);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable long id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@Valid @RequestBody CreatePostRequest request) {
        long createdPostId = postService.createPost(request);
        return ResponseEntity.created(URI.create("/posts/" + createdPostId)).build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> editPost(@PathVariable long id, @Valid @RequestBody EditPostRequest request) {
        postService.editPost(id, request);
        return ResponseEntity.noContent().build();
    }
}
