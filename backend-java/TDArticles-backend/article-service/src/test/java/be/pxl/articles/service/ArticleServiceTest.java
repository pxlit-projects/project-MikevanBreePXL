package be.pxl.articles.service;

import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.request.EditArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.domain.Article;
import be.pxl.articles.domain.Status;
import be.pxl.articles.repository.ArticleRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ComponentScan(basePackages = "be.pxl.articles")
@ActiveProfiles("test")
public class ArticleServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    private Article articleInMemory;

    @BeforeEach
    public void setUp() {
        articleInMemory = Article.builder()
                .id(351L)
                .title("Test Title")
                .content("Test content lorem ipsum")
                .status(Status.PENDING)
                .author("Auther The Testeth")
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findById(articleInMemory.getId())).thenReturn(Optional.of(articleInMemory));
    }

    @Test
    public void getArticle_ShouldReturnCorrectArticleById() {
        ArticleResponse articleResponse = articleService.getArticle(articleInMemory.getId());

        assertEquals(articleResponse.getId(), articleInMemory.getId());
        assertEquals(articleResponse.getTitle(), articleInMemory.getTitle());
        assertEquals(articleResponse.getContent(), articleInMemory.getContent());
        assertEquals(articleResponse.getAuthor(), articleInMemory.getAuthor());
    }

    @Test
    public void getPublishedArticles_ShouldReturnPublishedArticles() {
        Article publishedArticle = Article.builder()
                .id(62L)
                .title("The article of ever")
                .content("Read more here... (got 'em)")
                .author("The tester")
                .status(Status.PUBLISHED)
                .creationTime(LocalDateTime.now())
                .build();
        Article unpublishedArticle = Article.builder()
                .id(43L)
                .title("The article of ever")
                .content("Read more here... (got 'em)")
                .author("The tester")
                .status(Status.READY_TO_PUBLISH)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findAll()).thenReturn(List.of(articleInMemory, publishedArticle, unpublishedArticle));

        List<ArticleResponse> articles = articleService.getPublishedArticles(null, null, null, null);

        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getId(), publishedArticle.getId());
        assertEquals(articles.get(0).getTitle(), publishedArticle.getTitle());
        assertEquals(articles.get(0).getContent(), publishedArticle.getContent());
        assertEquals(articles.get(0).getAuthor(), publishedArticle.getAuthor());
        assertEquals(articles.get(0).getCreationTime(), publishedArticle.getCreationTime());
    }

    @Test
    public void getPendingArticles_ShouldReturnPendingArticles() {
        Article readyToPublishArticle = Article.builder()
                .id(121L)
                .title("The article of ever")
                .content("Read more here... (got 'em)")
                .author("The tester")
                .status(Status.READY_TO_PUBLISH)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findAll()).thenReturn(List.of(articleInMemory, readyToPublishArticle));

        List<ArticleResponse> articles = articleService.getPendingArticles();

        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getId(), articleInMemory.getId());
        assertEquals(articles.get(0).getTitle(), articleInMemory.getTitle());
        assertEquals(articles.get(0).getContent(), articleInMemory.getContent());
        assertEquals(articles.get(0).getAuthor(), articleInMemory.getAuthor());
        assertEquals(articles.get(0).getCreationTime(), articleInMemory.getCreationTime());
    }

    @Test
    public void getReadyToPublishArticles_ShouldReturnReadyToPublishArticles_FromAuthor() {
        Article readyToPublishArticle = Article.builder()
                .id(31L)
                .title("The article of ever")
                .content("Read more here... (got 'em)")
                .author("The tester")
                .status(Status.READY_TO_PUBLISH)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findAllByAuthor(Mockito.anyString())).thenReturn(List.of(articleInMemory, readyToPublishArticle));

        List<ArticleResponse> articles = articleService.getReadyToPublishArticles(readyToPublishArticle.getAuthor());

        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getId(), readyToPublishArticle.getId());
        assertEquals(articles.get(0).getTitle(), readyToPublishArticle.getTitle());
        assertEquals(articles.get(0).getContent(), readyToPublishArticle.getContent());
        assertEquals(articles.get(0).getAuthor(), readyToPublishArticle.getAuthor());
        assertEquals(articles.get(0).getCreationTime(), readyToPublishArticle.getCreationTime());
    }

    @Test
    public void getRejectedArticles_ShouldReturnRejectedArticles_FromAuthor() {
        Article rejectedArticle = Article.builder()
                .id(5L)
                .title("Not My Work")
                .content("NayNay")
                .author("The tester")
                .status(Status.DENIED)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findAllByAuthor(Mockito.anyString())).thenReturn(List.of(articleInMemory, rejectedArticle));

        List<ArticleResponse> articles = articleService.getRejectedArticles(rejectedArticle.getAuthor());

        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getId(), rejectedArticle.getId());
        assertEquals(articles.get(0).getTitle(), rejectedArticle.getTitle());
        assertEquals(articles.get(0).getContent(), rejectedArticle.getContent());
        assertEquals(articles.get(0).getAuthor(), rejectedArticle.getAuthor());
        assertEquals(articles.get(0).getCreationTime(), rejectedArticle.getCreationTime());
    }

    @Test
    public void publishReview_ShouldUpdateStatusToApproved_WhenPendingAndApproved() {
        boolean isApproved = true;
        Mockito.when(articleRepository.findAll()).thenReturn(List.of(articleInMemory));

        articleService.publishReview(articleInMemory.getId(), isApproved);

        Article reviewedArticle = articleRepository.findAll().get(0);

        assertEquals(reviewedArticle.getStatus(), Status.READY_TO_PUBLISH);
    }

    @Test
    public void publishReview_ShouldThrowException_WhenArticleIsNotPending() {
        boolean isApproved = true;
        Article articleInConcept = Article.builder()
                .id(3L)
                .title("Some article")
                .content("Read here")
                .author("The tester")
                .status(Status.CONCEPT)
                .creationTime(LocalDateTime.now().minusDays(2))
                .build();

        Mockito.when(articleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(articleInConcept));

        assertThrows(RuntimeException.class,
                () -> articleService.publishReview(articleInConcept.getId(), isApproved)
        );
    }

    @Test
    public void publishArticle_ShouldUpdateStatusToPublished_WhenReadyToPublish() {
        Article readyToPublishArticle = Article.builder()
                .id(22L)
                .title("The article")
                .content("Read more")
                .author("The tester")
                .status(Status.READY_TO_PUBLISH)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findById(readyToPublishArticle.getId())).thenReturn(Optional.of(readyToPublishArticle));
        Mockito.when(articleRepository.findAll()).thenReturn(List.of(readyToPublishArticle));

        articleService.publishArticle(readyToPublishArticle.getId(), true, readyToPublishArticle.getAuthor());

        Article publishedArticle = articleRepository.findAll().get(0);

        assertEquals(publishedArticle.getStatus(), Status.PUBLISHED);
    }

    @Test
    public void publishArticle_ShouldThrowException_WhenArticleIsNotOwnedByUser() {
        Article readyToPublishArticle = Article.builder()
                .id(1221L)
                .title("title")
                .content("description")
                .author("author")
                .status(Status.READY_TO_PUBLISH)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findById(readyToPublishArticle.getId())).thenReturn(Optional.of(readyToPublishArticle));
        Mockito.when(articleRepository.findAll()).thenReturn(List.of(readyToPublishArticle));

        assertThrows(RuntimeException.class,
                () -> articleService.publishArticle(readyToPublishArticle.getId(), true, "not" + readyToPublishArticle.getAuthor())
        );
    }

    @Test
    public void publishArticle_ShouldThrowException_WhenArticleIsNotReadyToPublish() {
        Article readyToPublishArticle = Article.builder()
                .id(1234L)
                .title("title")
                .content("description")
                .author("author")
                .status(Status.CONCEPT)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findById(readyToPublishArticle.getId())).thenReturn(Optional.of(readyToPublishArticle));
        Mockito.when(articleRepository.findAll()).thenReturn(List.of(readyToPublishArticle));

        assertThrows(RuntimeException.class,
                () -> articleService.publishArticle(readyToPublishArticle.getId(), true, "not" + readyToPublishArticle.getAuthor())
        );
    }

    @Test
    public void deleteArticle_ShouldDeleteArticleFromRepository_WhenAuthor() {
        Article articleToDelete = Article.builder()
                .id(348L)
                .title("The article of everEst")
                .content("Read more here...")
                .author("The tester")
                .status(Status.PUBLISHED)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findById(articleToDelete.getId())).thenReturn(Optional.of(articleToDelete));

        articleService.deleteArticle(articleToDelete.getId(), articleToDelete.getAuthor());

        Mockito.verify(articleRepository).delete(articleToDelete);
    }

    @Test
    public void deleteArticle_ShouldThrowException_WhenAuthorIsNotAuthor() {
        Article articleToDelete = Article.builder()
                .id(348L)
                .title("The article of everEst")
                .content("Read more here...")
                .author("The tester")
                .status(Status.PUBLISHED)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.when(articleRepository.findById(articleToDelete.getId())).thenReturn(Optional.of(articleToDelete));

        assertThrows(RuntimeException.class,
                () -> articleService.deleteArticle(articleToDelete.getId(), "not" + articleToDelete.getAuthor())
        );
    }

    @Test
    public void getConcepts_ShouldReturnAllConcepts_FromAuthor() {
        Article articleInConcept = Article.builder()
                .id(2134L)
                .author(articleInMemory.getAuthor())
                .title("titLe")
                .creationTime(LocalDateTime.now())
                .status(Status.CONCEPT)
                .build();
        Mockito.when(articleRepository.findAllByAuthor(articleInMemory.getAuthor())).thenReturn(List.of(articleInMemory, articleInConcept));

        List<ArticleResponse> articles = articleService.getConcepts(articleInMemory.getAuthor());

        assertEquals(1, articles.size());
        assertEquals(articleInConcept.getId().longValue(), articles.get(0).getId());
        assertEquals(articleInConcept.getTitle(), articles.get(0).getTitle());
        assertEquals(articleInConcept.getContent(), articles.get(0).getContent());
        assertEquals(articleInConcept.getCreationTime(), articles.get(0).getCreationTime());
        assertEquals(articleInConcept.getAuthor(), articles.get(0).getAuthor());
    }

    @Test
    public void testCreateArticle_ShouldSaveArticleInRepository() {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("title")
                .content("content")
                .author("author1")
                .concept(true)
                .build();

        Article savedArticle = Article.builder()
                .id(1L)
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .status(Status.CONCEPT)
                .build();

        Mockito.when(articleRepository.save(Mockito.any(Article.class))).thenReturn(savedArticle);

        articleService.createArticle(request);

        // Make sure .save() or .saveAndFlush() is called
        Mockito.verify(articleRepository, Mockito.times(1)).save(Mockito.any(Article.class));
    }

    @Test
    public void editArticle_ShouldEditSuccessfully() {
        EditArticleRequest editRequest = EditArticleRequest.builder()
                .title("the new title")
                .content("Some newer content")
                .concept(false)
                .build();

        articleService.editArticle(articleInMemory.getId(), editRequest);

        assertEquals(articleInMemory.getTitle(), editRequest.getTitle());
        assertEquals(articleInMemory.getContent(), editRequest.getContent());
        assertEquals(articleInMemory.getStatus(), Status.PENDING);
    }
}