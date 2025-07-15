package miu.edu.mpp.app.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAuthorKey implements Serializable {

    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "user_id")
    private Long userId;
}