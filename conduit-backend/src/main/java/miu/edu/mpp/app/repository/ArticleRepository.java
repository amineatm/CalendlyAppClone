package miu.edu.mpp.app.repository;


import miu.edu.mpp.app.domain.Article;
import miu.edu.mpp.app.domain.User;
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

    @Query("SELECT a FROM Article a " +
            "JOIN a.author u " +
            "JOIN u.following f " +
            "WHERE f.following = :userId " +
            "ORDER BY a.createdAt DESC")
    Page<Article> findArticlesByFollowedUsers(@Param("userId") Long userId, Pageable pageable);

}