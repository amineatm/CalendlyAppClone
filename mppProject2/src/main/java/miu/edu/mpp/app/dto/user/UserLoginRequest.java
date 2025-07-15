package miu.edu.mpp.app.dto.user;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserLoginRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}