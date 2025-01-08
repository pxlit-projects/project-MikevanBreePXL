package be.pxl.articles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class ArticleAlreadyPublishedException extends RuntimeException {
    public ArticleAlreadyPublishedException(String message) {
        super(message);
    }
}
