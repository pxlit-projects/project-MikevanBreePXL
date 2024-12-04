package be.pxl.articles.service;

import be.pxl.articles.domain.api.CreatePostRequest;
import be.pxl.articles.domain.api.EditPostRequest;
import be.pxl.articles.domain.api.PostResponse;

import java.util.List;

public interface IPostService {
    PostResponse getPost(long id);
    List<PostResponse> getAllPosts();
    long createPost(CreatePostRequest request);
    void editPost(long id, EditPostRequest request);
}
