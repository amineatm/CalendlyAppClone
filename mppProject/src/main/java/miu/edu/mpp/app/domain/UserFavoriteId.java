package miu.edu.mpp.app.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "article_id")
    private Long articleId;
}

