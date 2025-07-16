package miu.edu.mpp.app.repository;


import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.ArticleAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthor, Long> {
}