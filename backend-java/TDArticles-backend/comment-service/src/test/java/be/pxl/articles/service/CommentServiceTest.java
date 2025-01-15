package be.pxl.articles.service;

import be.pxl.articles.controller.request.CommentEditRequest;
import be.pxl.articles.controller.request.CommentSaveRequest;
import be.pxl.articles.controller.response.CommentDisplayResponse;
import be.pxl.articles.domain.Comment;
import be.pxl.articles.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "be.pxl.articles")
@ActiveProfiles("test")
class CommentServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private ICommentService commentService;

    private Comment commentInMemory;

    @BeforeEach
    void setUp() {
        commentInMemory = Comment.builder()
                .id(1342L)
                .articleId(43L)
                .comment("Test Comment")
                .author("Test Author")
                .creationTime(LocalDateTime.now().minusDays(43))
                .build();

        Mockito.when(commentRepository.findById(commentInMemory.getId())).thenReturn(Optional.of(commentInMemory));
        Mockito.when(commentRepository.findAllByArticleId(commentInMemory.getArticleId())).thenReturn(List.of(commentInMemory));
    }

    @Test
    void getAllCommentsFromArticle_ShouldReturnAllCommentsFromArticle_WithGivenArticleId() {
        List<CommentDisplayResponse> response = commentService.getAllCommentsFromArticle(commentInMemory.getArticleId());

        assertEquals(commentInMemory.getId(), response.getFirst().getId());
        assertEquals(commentInMemory.getArticleId(), response.getFirst().getArticleId());
        assertEquals(commentInMemory.getComment(), response.getFirst().getComment());
        assertEquals(commentInMemory.getAuthor(), response.getFirst().getAuthor());
        assertEquals(commentInMemory.getCreationTime(), response.getFirst().getCreationTime());
    }

    @Test
    void getCommentFromId_ShouldReturnComment_WithGivenCommentId() {
        CommentDisplayResponse response = commentService.getCommentFromId(commentInMemory.getId());

        assertEquals(commentInMemory.getId(), response.getId());
        assertEquals(commentInMemory.getArticleId(), response.getArticleId());
        assertEquals(commentInMemory.getComment(), response.getComment());
        assertEquals(commentInMemory.getAuthor(), response.getAuthor());
        assertEquals(commentInMemory.getCreationTime(), response.getCreationTime());
    }

    @Test
    void saveComment_ShouldSaveComment_WithCorrectValues() {
        CommentSaveRequest request = new CommentSaveRequest(commentInMemory.getArticleId(), "Test Comment");
        Comment comment = Comment.builder()
                .id(commentInMemory.getId() + 10)
                .articleId(request.getArticleId())
                .comment(request.getComment())
                .author("Test Author")
                .creationTime(LocalDateTime.now())
                .build();
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        commentService.saveComment(request, "Test Author");

        Mockito.verify(commentRepository).save(Mockito.any(Comment.class));
    }

    @Test
    void deleteComment_ShouldDeleteComment_WithExistingId() {
        commentService.deleteComment(commentInMemory.getId());

        Mockito.verify(commentRepository).delete(commentInMemory);
    }

    @Test
    void editComment_ShouldSuccessfullyUpdateComment_WithGivenValues() {
        CommentEditRequest request = new CommentEditRequest("Test Comment Updated");

        commentService.editComment(commentInMemory.getId(), request);

        Mockito.verify(commentRepository).save(Mockito.any(Comment.class));
        assertEquals(commentInMemory.getComment(), request.getComment());
    }
}