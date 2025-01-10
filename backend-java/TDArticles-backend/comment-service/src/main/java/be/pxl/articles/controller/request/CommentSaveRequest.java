package be.pxl.articles.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {
    @NotNull @JsonProperty("article_id")
    private Long articleId;
    @NotBlank
    private String comment;
    @NotBlank
    private String author;
}
