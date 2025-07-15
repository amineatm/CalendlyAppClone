package miu.edu.mpp.app.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "article_author")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAuthor {

    @EmbeddedId
    private ArticleAuthorKey id;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
