package be.pxl.articles.controller;

import be.pxl.articles.client.CommentClient;
import be.pxl.articles.controller.request.CreatePostRequest;
import be.pxl.articles.controller.request.EditPostRequest;
import be.pxl.articles.controller.response.PostResponse;
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
@RequestMapping("/")
public class PostController {
    private final IPostService postService;
    private final CommentClient commentClient;

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
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(@PathVariable String author) {
        List<PostResponse> posts = postService.getPostsByAuthor(author);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/concepts/{author}")
    public List<PostResponse> getConcepts(@PathVariable String author) {
        return postService.getConcepts(author);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable long id) {
        PostResponse post = postService.getPost(id);
        post.setComments(commentClient.getAllCommentsFromPost(id));

        return ResponseEntity.ok(post);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@Valid @RequestBody CreatePostRequest request) {
        long createdPostId = postService.createPost(request);
        return ResponseEntity.created(URI.create("/posts/" + createdPostId)).build();
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Void> editPost(@PathVariable long id, @Valid @RequestBody EditPostRequest request) {
        postService.editPost(id, request);
        return ResponseEntity.ok().build();
    }
}
