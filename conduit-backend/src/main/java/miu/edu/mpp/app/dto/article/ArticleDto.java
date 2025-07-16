package miu.edu.mpp.app.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ArticleDto {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tagList;
    private AuthorDto author;
    private int favoritesCount;
    private LocalDateTime lockedAt;
    private Long lockedBy;
    private List<CommentDto> comments;
    private boolean favorited;
    private List<?> authors;
    private List<?> collaboratorList;
    private boolean islocked;
}

