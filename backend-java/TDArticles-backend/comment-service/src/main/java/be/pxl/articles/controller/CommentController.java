package be.pxl.articles.controller;

import be.pxl.articles.controller.request.CommentEditRequest;
import be.pxl.articles.controller.request.CommentSaveRequest;
import be.pxl.articles.controller.response.CommentDisplayResponse;
import be.pxl.articles.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @ModelAttribute
    public void addAttributes(@RequestHeader("Username") String username) {
        // You can store the username in a field or use it as needed
    }

    @GetMapping("/{commentId}")
    public CommentDisplayResponse getCommentFromId(@PathVariable Long commentId) {
        return commentService.getCommentFromId(commentId);
    }

    @GetMapping("/article/{articleId}")
    public List<CommentDisplayResponse> getAllCommentsFromArticle(@PathVariable long articleId) {
        return commentService.getAllCommentsFromArticle(articleId);
    }

    @PostMapping("/")
    public ResponseEntity<Void> saveComment(@Valid @RequestBody CommentSaveRequest commentSaveRequest, @NotNull @RequestHeader("Username") String username) {
        long id = commentService.saveComment(commentSaveRequest, username);

        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> editComment(@PathVariable long commentId, @Valid @RequestBody CommentEditRequest commentEditRequest) {
        commentService.editComment(commentId, commentEditRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
