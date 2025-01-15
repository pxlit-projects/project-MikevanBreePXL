package be.pxl.articles.service;

import be.pxl.articles.controller.request.CommentEditRequest;
import be.pxl.articles.controller.request.CommentSaveRequest;
import be.pxl.articles.controller.response.CommentDisplayResponse;

import java.util.List;

public interface ICommentService {
    List<CommentDisplayResponse> getAllCommentsFromArticle(long articleId);

    CommentDisplayResponse getCommentFromId(Long commentId);

    long saveComment(CommentSaveRequest saveCommentRequest, String username);

    void deleteComment(long commentId);

    void editComment(long commentId, CommentEditRequest commentEditRequest);
}
