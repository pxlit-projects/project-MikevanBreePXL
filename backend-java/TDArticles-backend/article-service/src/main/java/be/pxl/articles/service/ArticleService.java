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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
            String authorUrlDecoded = URLDecoder.decode(author, StandardCharsets.UTF_8);
            articleStream = articleStream.filter(article -> article.getAuthor().toLowerCase().trim().contains(authorUrlDecoded.toLowerCase().trim()));
        }
        if (content != null) {
            String contentUrlDecoded = URLDecoder.decode(content, StandardCharsets.UTF_8).toLowerCase().trim();
            articleStream = articleStream.filter(article ->
                    article.getContent().toLowerCase().trim().contains(contentUrlDecoded) ||
                            article.getTitle().toLowerCase().trim().contains(contentUrlDecoded));
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
    public List<ArticleResponse> getReadyToPublishArticles(String author) {
        return articleRepository.findAllByAuthor(author).stream()
                .filter(article -> article.getStatus().equals(Status.READY_TO_PUBLISH))
                .map(article -> mapToArticleResponse(article))
                .toList();
    }

    @Override
    public void publishReview(long id, boolean approved) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Could not find article with id " + id));
        if (approved) {
            article.setStatus(Status.READY_TO_PUBLISH);
        } else {
            article.setStatus(Status.DENIED);
        }
        articleRepository.save(article);
    }

    @Override
    public void publishArticle(long id, boolean publish, String username) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Could not find article with id " + id));

        if (!article.getAuthor().equals(username)) {
            throw new ArticleNotFoundException("You are not the author of this article");
        }
        if (!article.getStatus().equals(Status.READY_TO_PUBLISH)) {
            throw new ArticleNotFoundException("Article is not ready to publish");
        }

        if (publish) {
            article.setStatus(Status.PUBLISHED);
        } else {
            article.setStatus(Status.CONCEPT);
        }
        article.setCreationTime(LocalDateTime.now());

        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(long id, String username) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Could not find article with id " + id));

        if (!article.getAuthor().equals(username)) {
            throw new ArticleNotFoundException("You are not the author of this article");
        }

        articleRepository.delete(article);
        articleRepository.flush();
    }

    public List<ArticleResponse> getConcepts(String author) {
        return articleRepository.findAllByAuthor(author).stream()
                .filter(article -> article.getStatus().equals(Status.CONCEPT))
                .map(article -> mapToArticleResponse(article))
                .toList();
    }

    public List<ArticleResponse> getDeniedArticles(String author) {
        return articleRepository.findAllByAuthor(author).stream()
                .filter(article -> article.getStatus().equals(Status.DENIED))
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

        // POST Submission only -- checks concept update status
        if (article.getStatus().equals(Status.CONCEPT)
        || article.getStatus().equals(Status.DENIED)) {
            if (request.getConcept()) {
                article.setStatus(Status.CONCEPT);
            } else {
                article.setStatus(Status.PENDING);
            }
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
