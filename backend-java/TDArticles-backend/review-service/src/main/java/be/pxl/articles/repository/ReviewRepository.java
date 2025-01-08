package be.pxl.articles.repository;

import be.pxl.articles.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByArticleId(Long articleId);
}
