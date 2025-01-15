package be.pxl.articles.controller;

import be.pxl.articles.client.CommentClient;
import be.pxl.articles.controller.request.CreateArticleRequest;
import be.pxl.articles.controller.response.ArticleResponse;
import be.pxl.articles.domain.Article;
import be.pxl.articles.domain.Status;
import be.pxl.articles.repository.ArticleRepository;
import be.pxl.articles.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
public class ArticleControllerTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private CommentClient commentClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getPublishedArticles_ShouldReturnPublishedArticles_WithGivenParameters() throws Exception {
        Article article = Article.builder()
                .id(1L)
                .title("Test Article")
                .content("TestContent")
                .author("TestAuthor")
                .status(Status.PUBLISHED)
                .creationTime(LocalDateTime.now().minusDays(2))
                .build();

        ArticleResponse response = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();

        when(articleService.getPublishedArticles(any(LocalDateTime.class), any(LocalDateTime.class), Mockito.eq(response.getAuthor()), Mockito.anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/?from=" + LocalDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME) + "&to=" + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME) + "&author=" + article.getAuthor() + "&content=" + article.getContent())
                        .header("Username", article.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(article.getId()))
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()))
                .andExpect(jsonPath("$[0].author").value(article.getAuthor()));
    }

    @Test
    public void getPendingArticles_ShouldReturnPendingArticles() throws Exception {
        Article article = Article.builder()
                .id(1L)
                .title("Test Article")
                .content("Test Content")
                .author("Test Author")
                .status(Status.PENDING)
                .creationTime(LocalDateTime.now())
                .build();

        ArticleResponse response = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();

        when(articleService.getPendingArticles()).thenReturn(List.of(response));

        mockMvc.perform(get("/pending")
                        .header("Username", article.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(article.getId()))
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()))
                .andExpect(jsonPath("$[0].author").value(article.getAuthor()));
    }

    @Test
    public void createArticle_ShouldCreateArticleSuccessfully() throws Exception {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("New Article")
                .content("New Content")
                .author("Author")
                .concept(false)
                .build();

        when(articleService.createArticle(any(CreateArticleRequest.class))).thenReturn(6L);

        mockMvc.perform(post("/create")
                        .header("Username", request.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/article/6"));
    }

    @Test
    public void getReadyToPublishArticles_ShouldReturnReadyToPublishArticlesFromAuthor_WithUsernameInHeader() throws Exception {
        Article article = Article.builder()
                .id(1L)
                .title("Test Article")
                .content("Test Content")
                .author("Test Author")
                .status(Status.PENDING)
                .creationTime(LocalDateTime.now())
                .build();

        ArticleResponse response = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();

        when(articleService.getReadyToPublishArticles(response.getAuthor())).thenReturn(List.of(response));

        mockMvc.perform(get("/publish-ready")
                        .header("Username", response.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(article.getId()))
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()))
                .andExpect(jsonPath("$[0].author").value(article.getAuthor()));
    }

    @Test
    public void getRejectedArticles_ShouldReturnRejectedArticlesFromAuthor_WithUsernameInHeader() throws Exception {
        Article article = Article.builder()
                .id(1L)
                .title("Test Article")
                .content("Test Content")
                .author("Test Author")
                .status(Status.DENIED)
                .creationTime(LocalDateTime.now())
                .build();

        ArticleResponse response = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();

        when(articleService.getRejectedArticles(response.getAuthor())).thenReturn(List.of(response));

        mockMvc.perform(get("/review")
                        .header("Username", response.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(article.getId()))
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()))
                .andExpect(jsonPath("$[0].author").value(article.getAuthor()));
    }

    @Test
    public void getConcepts_ShouldReturnConceptsFromAuthor_WithUsernameInHeader() throws Exception {
        Article article = Article.builder()
                .id(1L)
                .title("Test Article")
                .content("Test Content")
                .author("Test Author")
                .status(Status.CONCEPT)
                .creationTime(LocalDateTime.now())
                .build();

        ArticleResponse response = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();

        when(articleService.getConcepts(response.getAuthor())).thenReturn(List.of(response));

        mockMvc.perform(get("/concepts")
                        .header("Username", response.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(article.getId()))
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()))
                .andExpect(jsonPath("$[0].author").value(article.getAuthor()));
    }

    @Test
    public void getArticleById_ShouldReturnArticle_WithGivenId() throws Exception {
        Article article = Article.builder()
                .id(1L)
                .title("Test Article")
                .content("Test Content")
                .author("Test Author")
                .status(Status.PUBLISHED)
                .creationTime(LocalDateTime.now().minusDays(2))
                .build();

        ArticleResponse response = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .creationTime(article.getCreationTime())
                .build();

        when(articleService.getArticle(article.getId())).thenReturn(response);

        mockMvc.perform(get("/{id}", article.getId())
                        .header("Username", response.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(article.getId()))
                .andExpect(jsonPath("$.title").value(article.getTitle()))
                .andExpect(jsonPath("$.content").value(article.getContent()))
                .andExpect(jsonPath("$.author").value(article.getAuthor()));
    }

    @Test
    public void createArticle_ShouldCreateArticle_WithGivenParameters() throws Exception {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("New Article")
                .content("New Content")
                .author("Author")
                .concept(false)
                .build();

        when(articleService.createArticle(any(CreateArticleRequest.class))).thenReturn(6L);

        mockMvc.perform(post("/create")
                        .header("Username", request.getAuthor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/article/6"));
    }

    @Test
    public void publishReview_ShouldUpdateStatus_WhenPendingAndApproved() throws Exception {
        boolean isApproved = true;
        mockMvc.perform(post("/{id}/publish/review", 1L)
                        .header("Username", "TestAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(isApproved)))
                .andExpect(status().isOk());
    }
}