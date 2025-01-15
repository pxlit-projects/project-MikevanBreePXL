package be.pxl.articles.service;

import be.pxl.articles.client.ArticleClient;
import be.pxl.articles.controller.request.NotificationRequest;
import be.pxl.articles.controller.request.ReviewRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.controller.response.ReviewResponse;
import be.pxl.articles.domain.Review;
import be.pxl.articles.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
class ReviewServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management")
            .withExposedPorts(5672, 15672)
            .withUser("guest", "guest")
            .withVhost("/")
            .withPermission("/", "guest", ".*", ".*", ".*");

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ArticleClient articleClient;

    @Autowired
    private ReviewService reviewService;

    private Review reviewInMemory;

    @BeforeEach
    void setUp() {
        reviewInMemory = Review.builder()
                .id(16L)
                .articleId(69L)
                .approved(true)
                .rejectionNotes("Test Notes")
                .build();
        when(reviewRepository.findAllByArticleId(reviewInMemory.getArticleId())).thenReturn(List.of(reviewInMemory));
        when(reviewRepository.findById(reviewInMemory.getId())).thenReturn(Optional.of(reviewInMemory));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(reviewInMemory);

        rabbitMQContainer.start();
    }

    @Test
    void getPendingArticles_ShouldReturnPendingArticles() {
        ArticleResponse articleOne = ArticleResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .author("Test Author")
                .creationTime(null)
                .build();

        ArticleResponse articleTwo = ArticleResponse.builder()
                .id(2L)
                .title("Test Title")
                .content("Test Content")
                .author("Test Author")
                .creationTime(null)
                .build();

        when(articleClient.getPendingArticles("Test Author")).thenReturn(List.of(articleOne, articleTwo));

        List<ArticleResponse> articles = reviewService.getPendingArticles("Test Author");

        assertEquals(2, articles.size());

        assertEquals(articleOne.getId(), articles.get(0).getId());
        assertEquals(articleOne.getTitle(), articles.get(0).getTitle());
        assertEquals(articleOne.getContent(), articles.get(0).getContent());
        assertEquals(articleOne.getAuthor(), articles.get(0).getAuthor());

        assertEquals(articleTwo.getId(), articles.get(1).getId());
        assertEquals(articleTwo.getTitle(), articles.get(1).getTitle());
        assertEquals(articleTwo.getContent(), articles.get(1).getContent());
        assertEquals(articleTwo.getAuthor(), articles.get(1).getAuthor());
    }

    @Test
    void getReviewByArticleId_ShouldReturnReview_WithGivenArticleId() {
        ReviewResponse response = reviewService.getReviewByArticleId(reviewInMemory.getArticleId());

        assertEquals(reviewInMemory.getId(), response.getId());
        assertEquals(reviewInMemory.getArticleId(), response.getArticleId());
        assertEquals(reviewInMemory.isApproved(), response.isApproved());
        assertEquals(reviewInMemory.getRejectionNotes(), response.getRejectionNotes());
    }

    @Test
    void postReview_ShouldSaveReview_WithCorrectValues() {
        ReviewRequest request = ReviewRequest.builder()
                .approved(true)
                .rejectionNotes(null)
                .build();
        when(articleClient.publishReview(reviewInMemory.getArticleId(), "Test Author", true)).thenReturn(ResponseEntity.ok().build());
        doNothing().when(rabbitTemplate).convertAndSend(Mockito.anyString(), Mockito.any(NotificationRequest.class));

        reviewService.postReview(reviewInMemory.getArticleId() + 1, request, "Test Author");

        Mockito.verify(reviewRepository).save(Mockito.any(Review.class));
    }
}