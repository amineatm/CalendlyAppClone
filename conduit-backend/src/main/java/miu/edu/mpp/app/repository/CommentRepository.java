package miu.edu.mpp.app.repository;

import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.Comment;
import miu.edu.mpp.app.domain.Tag;
import miu.edu.mpp.app.dto.article.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);
    List<CommentResponse> getCommentsByArticleSlug(String slug);

}