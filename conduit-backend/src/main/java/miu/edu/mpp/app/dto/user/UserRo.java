package miu.edu.mpp.app.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRo {
    private final String username;
    private final String email;
    private final String bio;
    private final String image;
    private final String token;
}
