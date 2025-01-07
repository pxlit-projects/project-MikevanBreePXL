package be.pxl.articles.service;

import be.pxl.articles.controller.request.CommentEditRequest;
import be.pxl.articles.controller.request.CommentSaveRequest;
import be.pxl.articles.controller.response.CommentDisplayResponse;
import be.pxl.articles.domain.Comment;
import be.pxl.articles.exception.CommentNotFoundException;
import be.pxl.articles.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDisplayResponse> getAllCommentsFromPost(long postId) {
        return commentRepository.findAllByPostId(postId).stream()
                .map(this::createDisplayResponseFromEntity)
                .toList();
    }

    public CommentDisplayResponse getCommentFromId(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Could not find comment with id: " + commentId));

        return createDisplayResponseFromEntity(comment);
    }

    public long saveComment(CommentSaveRequest saveCommentRequest) {
        Comment newComment = createEntityFromSaveRequest(saveCommentRequest);
        return commentRepository.save(newComment).getId();
    }

    public void deleteComment(long commentId) {
        Comment commentToRemove = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Could not find comment with id: " + commentId));

        commentRepository.delete(commentToRemove);
    }

    public void editComment(long commentId, CommentEditRequest commentEditRequest) {
        Comment commentToEdit = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Could not find comment with id: " + commentId));

        commentToEdit.setComment(commentEditRequest.getComment());
        commentToEdit.setAuthor(commentEditRequest.getAuthor());

        commentRepository.save(commentToEdit);
    }

    private Comment createEntityFromSaveRequest(CommentSaveRequest commentSaveRequest) {
        return Comment.builder()
                .postId(commentSaveRequest.getPostId())
                .comment(commentSaveRequest.getComment())
                .author(commentSaveRequest.getAuthor())
                .authorId(commentSaveRequest.getAuthorId())
                .creationTime(LocalDateTime.now())
                .build();
    }

    private CommentDisplayResponse createDisplayResponseFromEntity(Comment comment) {
        return CommentDisplayResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .comment(comment.getComment())
                .author(comment.getAuthor())
                .authorId(comment.getAuthorId())
                .creationTime(comment.getCreationTime())
                .build();
    }
}
