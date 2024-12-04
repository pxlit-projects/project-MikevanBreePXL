package be.pxl.articles.service;

import be.pxl.articles.domain.api.CreatePostRequest;
import be.pxl.articles.domain.api.EditPostRequest;
import be.pxl.articles.domain.api.PostResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {
    List<PostResponse> getPublishedPosts(LocalDateTime from, LocalDateTime to, String author, String content);
    PostResponse getPost(long id);
    long createPost(CreatePostRequest request);
    void editPost(long id, EditPostRequest request);
}
