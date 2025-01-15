package be.pxl.articles.exceptions;

public class ArticleNotReadyException extends RuntimeException {
    public ArticleNotReadyException(String message) {
        super(message);
    }
}
