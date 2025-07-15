package miu.edu.mpp.app.repository;


import miu.edu.mpp.app.domain.ArticleAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthor, Long> {
}