package miu.edu.mpp.app.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUser {
    private Long id;
    private String email;
    private String username;

    CurrentUser() {}
}