package com.calendly.backend.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
