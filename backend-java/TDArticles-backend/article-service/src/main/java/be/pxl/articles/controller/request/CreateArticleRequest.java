package be.pxl.articles.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleRequest {
        @NotBlank(message = "Article must have a title")
        @Length(min = 3, max = 200, message = "Title must be in between 3 and 200 characters")
        private String title;
        @NotBlank(message = "Article must have content")
        @Length(min = 3, message = "Content must be more than 2 characters")
        private String content;
        @NotBlank(message = "Article must have an author")
        @Length(min = 3, message = "Author's name must be more than 2 characters")
        private String author;
        @NotNull
        @JsonProperty("concept")
        private Boolean concept;
}
