package be.pxl.articles.service;

import be.pxl.articles.domain.Post;
import be.pxl.articles.domain.api.CreatePostRequest;
import be.pxl.articles.domain.api.EditPostRequest;
import be.pxl.articles.domain.api.PostResponse;
import be.pxl.articles.exceptions.PostAlreadyPublishedException;
import be.pxl.articles.exceptions.PostNotFoundException;
import be.pxl.articles.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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
    public List<PostResponse> getPublishedPosts(LocalDateTime from, LocalDateTime to, String author, String content) {
        Stream<Post> posts = postRepository.findAll()
                .stream()
                .filter(post -> !post.isConcept());

        if (from != null) {
            posts = posts.filter(post -> post.getCreationTime().isAfter(from));
        }
        if (to != null) {
            posts = posts.filter(post -> post.getCreationTime().isBefore(to));
        }
        if (author != null) {
            posts = posts.filter(post -> post.getAuthor().equals(author));
        }
        if (content != null) {
            posts = posts.filter(post -> (post.getContent().contains(content) || post.getTitle().contains(content)));
        }

        return posts.map(post -> mapToPostResponse(post))
                .toList();
    }

    @Override
    public long createPost(CreatePostRequest request) {
        Post newPost = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .concept(request.getConcept())
                .creationTime(LocalDateTime.now())
                .build();
        return postRepository.save(newPost).getId();
    }

    @Override
    public void editPost(long id, EditPostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Could not find post with id " + id));
        if (!post.isConcept() && request.getConcept()) {
            throw new PostAlreadyPublishedException("Post is already published and cannot be set to a concept");
        }

        if (post.isConcept()) {
            post.setCreationTime(LocalDateTime.now());
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setConcept(request.getConcept());

        postRepository.save(post);
    }

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .concept(post.isConcept())
                .creationTime(post.getCreationTime())
                .build();
    }
}
