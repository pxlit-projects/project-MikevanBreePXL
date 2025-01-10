package be.pxl.articles.service;

import be.pxl.articles.domain.Article;
import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.request.EditArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.domain.Status;
import be.pxl.articles.exceptions.ArticleAlreadyPublishedException;
import be.pxl.articles.exceptions.ArticleNotFoundException;
import be.pxl.articles.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepository;

    @Override
    public ArticleResponse getArticle(long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Article with id " + id + " not found"));
        return mapToArticleResponse(article);
    }

    @Override
    public List<ArticleResponse> getPublishedArticles(LocalDateTime from, LocalDateTime to, String author, String content) {
        Stream<Article> articleStream = articleRepository.findAll()
                .stream()
                .filter(article -> article.getStatus().equals(Status.PUBLISHED));

        if (from != null) {
            articleStream = articleStream.filter(article -> article.getCreationTime().isAfter(from));
        }
        if (to != null) {
            articleStream = articleStream.filter(article -> article.getCreationTime().isBefore(to));
        }
        if (author != null) {
            articleStream = articleStream.filter(article -> article.getAuthor().equals(author));
        }
        if (content != null) {
            articleStream = articleStream.filter(article -> (article.getContent().contains(content) || article.getTitle().contains(content)));
        }

        return articleStream.map(article -> mapToArticleResponse(article))
                .toList();
    }

    @Override
    public List<ArticleResponse> getPendingArticles() {
        return articleRepository.findAll().stream()
                .filter(article -> article.getStatus().equals(Status.PENDING))
                .map(article -> mapToArticleResponse(article))
                .toList();
    }

    @Override
    public void publishArticle(long id, boolean approved) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Could not find article with id " + id));
        if (approved) {
            article.setStatus(Status.PUBLISHED);
        } else {
            article.setStatus(Status.DENIED);
        }
        articleRepository.save(article);
    }

    public List<ArticleResponse> getConcepts(String author) {
        return articleRepository.findAllByAuthor(author).stream()
                .filter(article -> article.getStatus().equals(Status.CONCEPT))
                .map(article -> mapToArticleResponse(article))
                .toList();
    }

    @Override
    public List<ArticleResponse> getArticlesByAuthor(String author) {
        return articleRepository.findAllByAuthor(author)
                .stream()
                .map(article -> mapToArticleResponse(article))
                .toList();
    }

    @Override
    public long createArticle(CreateArticleRequest request) {
        Article.ArticleBuilder newArticle = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .creationTime(LocalDateTime.now());

        if (request.getConcept()) {
            newArticle.status(Status.CONCEPT);
        } else {
            newArticle.status(Status.PENDING);
        }

        return articleRepository.save(newArticle.build()).getId();
    }

    @Override
    public void editArticle(long id, EditArticleRequest request) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Could not find article with id " + id));
        if (!article.getStatus().equals(Status.CONCEPT) && request.getConcept()) {
            throw new ArticleAlreadyPublishedException("Article is already published and cannot be set to a concept");
        }

        if (article.getStatus().equals(Status.CONCEPT)) {
            article.setCreationTime(LocalDateTime.now());
        }
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        if (request.getConcept()) {
            article.setStatus(Status.CONCEPT);
        } else {
            article.setStatus(Status.PENDING);
        }

        articleRepository.save(article);
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();
    }
}
