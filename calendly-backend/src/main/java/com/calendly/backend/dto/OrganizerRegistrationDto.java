package com.calendly.backend.dto;

import lombok.Data;

@Data
public class OrganizerRegistrationDto {
    private String name;
    private String email;
    private String password;
    private String role;
}
