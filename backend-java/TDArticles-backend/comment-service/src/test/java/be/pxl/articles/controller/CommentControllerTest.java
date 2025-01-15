package be.pxl.articles.controller;

import be.pxl.articles.controller.request.CommentEditRequest;
import be.pxl.articles.controller.request.CommentSaveRequest;
import be.pxl.articles.controller.response.CommentDisplayResponse;
import be.pxl.articles.domain.Comment;
import be.pxl.articles.repository.CommentRepository;
import be.pxl.articles.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
class CommentControllerTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getCommentFromId_ShouldReturnComment_WithGivenCommentId() throws Exception {
        Comment comment = Comment.builder()
                .id(1L)
                .articleId(1L)
                .comment("Test Comment")
                .author("Test Author")
                .creationTime(LocalDateTime.now().minusDays(1))
                .build();


        CommentDisplayResponse response = CommentDisplayResponse.builder()
                .id(comment.getId())
                .articleId(comment.getArticleId())
                .comment(comment.getComment())
                .author(comment.getAuthor())
                .creationTime(comment.getCreationTime())
                .build();

        when(commentService.getCommentFromId(comment.getId())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/{commentId}", comment.getId())
                        .header("Username", comment.getAuthor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.articleId").value(comment.getArticleId()))
                .andExpect(jsonPath("$.comment").value(comment.getComment()))
                .andExpect(jsonPath("$.author").value(comment.getAuthor()))
                .andExpect(jsonPath("$.creationTime").value(comment.getCreationTime().format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    void getAllCommentsFromArticle_ShouldReturnListOfComments_WithGivenArticleId() throws Exception {
        CommentDisplayResponse response = CommentDisplayResponse.builder()
                .id(6L)
                .articleId(13L)
                .comment("Test Comment")
                .author("Test Author")
                .creationTime(LocalDateTime.now().minusDays(1))
                .build();

        when(commentService.getAllCommentsFromArticle(response.getArticleId())).thenReturn(List.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get("/article/{articleId}", response.getArticleId())
                        .header("Username", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId()))
                .andExpect(jsonPath("$[0].articleId").value(response.getArticleId()))
                .andExpect(jsonPath("$[0].comment").value(response.getComment()))
                .andExpect(jsonPath("$[0].author").value(response.getAuthor()));
    }

    @Test
    void saveComment_ShouldSaveCommentAndReturn200_Ok_WithCorrectValues() throws Exception {
        CommentSaveRequest request = new CommentSaveRequest(13L, "Test Comment");

        when(commentService.saveComment(Mockito.any(CommentSaveRequest.class), Mockito.anyString())).thenReturn(6L);
        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .header("Username", "The tester")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/" + 6L));

        verify(commentService).saveComment(Mockito.eq(request), Mockito.anyString());    }

    @Test
    void editComment_ShouldCallSave_WithCorrectValues() throws Exception {
        CommentEditRequest request = new CommentEditRequest("Test Comment");

        mockMvc.perform(MockMvcRequestBuilders.put("/{commentId}", 1L)
                        .header("Username", "The tester")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(commentService).editComment(1L, request);
    }

    @Test
    void deleteComment_ShouldCallDeleteAndShow204NoContent_WithExistingCommentId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/{commentId}", 1L)
                        .header("Username", "The tester"))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(1L);
    }
}