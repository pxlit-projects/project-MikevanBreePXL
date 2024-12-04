package be.pxl.articles.domain.api;

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
public class EditPostRequest {
    @NotBlank(message = "Title cannot be empty")
    @Length(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;
    @NotBlank(message = "Content cannot be empty")
    @Length(min = 3, message = "Content must be more than 2 characters")
    private String content;
    @NotNull
    private Boolean concept;
}
