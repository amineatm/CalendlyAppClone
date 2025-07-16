package miu.edu.mpp.app.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
}
