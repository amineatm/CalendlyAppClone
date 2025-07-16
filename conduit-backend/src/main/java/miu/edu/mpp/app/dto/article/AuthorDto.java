package miu.edu.mpp.app.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorDto {
    private Long id;
    private String username;
    private String bio;
    private String image;
    private boolean following;
    private String email;
}
