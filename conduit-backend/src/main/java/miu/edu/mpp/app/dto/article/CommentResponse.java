package miu.edu.mpp.app.dto.article;

import lombok.Builder;
import lombok.Data;
import miu.edu.mpp.app.dto.user.UserRo;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String body;
    private Long article;
    private UserRo author;
}
