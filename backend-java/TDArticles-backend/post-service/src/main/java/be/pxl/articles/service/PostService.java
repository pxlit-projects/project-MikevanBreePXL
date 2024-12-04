package be.pxl.articles.service;

import be.pxl.articles.domain.Post;
import be.pxl.articles.domain.api.CreatePostRequest;
import be.pxl.articles.domain.api.PostResponse;
import be.pxl.articles.exceptions.PostNotFoundException;
import be.pxl.articles.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;

    @Override
    public PostResponse getPost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        return mapToPostResponse(post);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> mapToPostResponse(post))
                .toList();
    }

    @Override
    public long createPost(CreatePostRequest request) {
        Post newPost = Post.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .author(request.getAuthor())
                        .build();
        return postRepository.save(newPost).getId();
    }

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .creationTime(post.getCreationTime())
                .build();
    }
}
