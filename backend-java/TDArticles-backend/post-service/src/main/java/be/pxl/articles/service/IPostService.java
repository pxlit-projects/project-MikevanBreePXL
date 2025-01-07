package be.pxl.articles.service;

import be.pxl.articles.controller.request.CreatePostRequest;
import be.pxl.articles.controller.request.EditPostRequest;
import be.pxl.articles.controller.response.PostResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {
    List<PostResponse> getPublishedPosts(LocalDateTime from, LocalDateTime to, String author, String content);
    PostResponse getPost(long id);
    List<PostResponse> getPostsByAuthor(String author);
    long createPost(CreatePostRequest request);
    void editPost(long id, EditPostRequest request);
}
