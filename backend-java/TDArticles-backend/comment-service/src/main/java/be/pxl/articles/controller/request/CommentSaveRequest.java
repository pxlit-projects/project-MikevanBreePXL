package be.pxl.articles.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {
    @NotNull @JsonProperty("post_id")
    private Long postId;
    @NotBlank
    private String comment;
    @NotBlank
    private String author;
    @NotNull
    private int authorId;
}
