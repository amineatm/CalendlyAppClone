package miu.edu.mpp.app.repository;


import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.article.RoasterUserArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long>, JpaSpecificationExecutor<Article> {

    @Query("SELECT a FROM Article a WHERE a.author IN :authors ORDER BY a.createdAt DESC")
    Page<Article> findByAuthorsIn(@Param("authors") List<User> authors, Pageable pageable);

    @Query(value = "SELECT u.id AS id, " +
            "u.username AS username, " +
            "COUNT(DISTINCT a.id) AS totalArticlesWritten, " +
            "COALESCE(COUNT(uf.article_id), 0) AS totalLikesReceived, " +
            "MIN(a.created_at) AS firstArticleDate " +
            "FROM article a " +
            "LEFT JOIN user u ON a.author_id = u.id " +
            "LEFT JOIN user_favorites uf ON uf.article_id = a.id " +
            "GROUP BY u.id, u.username " +
            "ORDER BY totalLikesReceived DESC " +
            "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<RoasterUserArticle> findRoasterUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT a FROM Article a WHERE a.tagList LIKE %:tag%")
    Page<Article> findByTagContaining(@Param("tag") String tag, Pageable pageable);

    Optional<Article> findBySlug(String slug);


    @Query("SELECT a FROM Article a " +
            "JOIN a.author u " +
            "JOIN u.following f " +
            "WHERE f.following = :userId " +
            "ORDER BY a.createdAt DESC")
    Page<Article> findArticlesByFollowedUsers(@Param("userId") Long userId, Pageable pageable);

}