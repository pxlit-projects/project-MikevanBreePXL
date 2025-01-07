package be.pxl.articles.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDisplayResponse {
    private Long id;
    private Long postId;
    private String comment;
    private String author;
    private int authorId;
    private LocalDateTime creationTime;
}
