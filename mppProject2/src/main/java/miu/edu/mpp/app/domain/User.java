package miu.edu.mpp.app.domain;

import lombok.Data;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "article_authors",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private Set<Article> contributedArticles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private Set<Article> favoriteArticles = new HashSet<>();
}
