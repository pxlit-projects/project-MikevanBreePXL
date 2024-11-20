package be.pxl.articles.domain.api;

public record CreatePostRequest(
        String title,
        String content,
        String author
) { }
