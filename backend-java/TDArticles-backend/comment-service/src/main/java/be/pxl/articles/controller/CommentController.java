package be.pxl.articles.controller;

import be.pxl.articles.controller.request.CommentEditRequest;
import be.pxl.articles.controller.request.CommentSaveRequest;
import be.pxl.articles.controller.response.CommentDisplayResponse;
import be.pxl.articles.service.CommentService;
import jakarta.validation.Valid;
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

    @GetMapping("/{commentId}")
    public CommentDisplayResponse getCommentFromId(@PathVariable Long commentId) {
        return commentService.getCommentFromId(commentId);
    }

    @GetMapping("/post/{postId}")
    public List<CommentDisplayResponse> getAllCommentsFromPost(@PathVariable long postId) {
        return commentService.getAllCommentsFromPost(postId);
    }

    @PostMapping("/")
    public ResponseEntity<Void> saveComment(@Valid @RequestBody CommentSaveRequest commentSaveRequest) {
        long id = commentService.saveComment(commentSaveRequest);

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
