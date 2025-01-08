package be.pxl.articles.service;

import be.pxl.articles.domain.Article;
import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.request.EditArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;
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
                .filter(article -> !article.isConcept());

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

    public List<ArticleResponse> getConcepts(String author) {
        return articleRepository.findAllByAuthor(author).stream()
                .filter(Article::isConcept)
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
        Article newArticle = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .concept(request.getConcept())
                .creationTime(LocalDateTime.now())
                .build();
        return articleRepository.save(newArticle).getId();
    }

    @Override
    public void editArticle(long id, EditArticleRequest request) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Could not find article with id " + id));
        if (!article.isConcept() && request.getConcept()) {
            throw new ArticleAlreadyPublishedException("Article is already published and cannot be set to a concept");
        }

        if (article.isConcept()) {
            article.setCreationTime(LocalDateTime.now());
        }
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setConcept(request.getConcept());

        articleRepository.save(article);
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .concept(article.isConcept())
                .creationTime(article.getCreationTime())
                .build();
    }
}
