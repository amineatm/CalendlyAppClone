package miu.edu.mpp.app.security;

import lombok.Data;

@Data
public class CurrentUser {
    private Long id;
    private String email;
    private String username;
}