package be.pxl.articles.domain.api;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    public long id;
    public String title;
    public String content;
    public String author;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime creationTime;
}
