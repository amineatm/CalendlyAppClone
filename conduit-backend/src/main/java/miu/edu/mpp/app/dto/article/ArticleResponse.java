package miu.edu.mpp.app.dto.article;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import miu.edu.mpp.app.dto.user.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ArticleResponse {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tagList;
    private int favoritesCount;
    private boolean favorited;
    private UserResponse author;
    private Boolean islocked;
    private Boolean collaboratorsAdded;


}
