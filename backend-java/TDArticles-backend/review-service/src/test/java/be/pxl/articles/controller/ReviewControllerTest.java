package be.pxl.articles.controller;

import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.controller.response.ReviewResponse;
import be.pxl.articles.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "be.pxl.articles")
@ActiveProfiles("test")
class ReviewControllerTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ReviewController reviewController;

    @Test
    void getPendingReviews_ShouldCallService_WithUsernameInHeader() throws Exception {
        ArticleResponse response = ArticleResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .author("Test Author")
                .creationTime(null)
                .build();

        when(reviewService.getPendingArticles(response.getAuthor())).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .header("Username", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId()))
                .andExpect(jsonPath("$[0].title").value(response.getTitle()))
                .andExpect(jsonPath("$[0].content").value(response.getContent()))
                .andExpect(jsonPath("$[0].author").value(response.getAuthor()));

        verify(reviewService, Mockito.times(1)).getPendingArticles("Test Author");
    }

    @Test
    void getReview_ShouldCallService_WithUsernameInHeader() throws Exception {
        ReviewResponse response = ReviewResponse.builder()
                .id(1L)
                .articleId(43L)
                .approved(true)
                .rejectionNotes(null)
                .build();

        when(reviewService.getReviewByArticleId(response.getId())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/{articleId}", response.getId())
                        .header("Username", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.articleId").value(response.getArticleId()))
                .andExpect(jsonPath("$.approved").value(response.isApproved()))
                .andExpect(jsonPath("$.rejectionNotes").value(response.getRejectionNotes()));

        verify(reviewService, Mockito.times(1)).getReviewByArticleId(response.getId());
    }
}