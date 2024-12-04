package be.pxl.articles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class PostAlreadyPublishedException extends RuntimeException {
    public PostAlreadyPublishedException(String message) {
        super(message);
    }
}
