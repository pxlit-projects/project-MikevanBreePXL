package be.pxl.articles.domain.api;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
        @NotBlank(message = "Post must have a title")
        @Length(min = 3, message = "Title must be more than 2 characters")
        String title;
        @NotBlank(message = "Post must have content")
        @Length(min = 3, message = "Content must be more than 2 characters")
        String content;
        @NotBlank(message = "Post must have an author")
        @Length(min = 3, message = "Author's name must be more than 2 characters")
        String author;
}
