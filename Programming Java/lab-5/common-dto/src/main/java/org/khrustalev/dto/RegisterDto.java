package org.khrustalev.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDto {
    private String name;
    private String password;
    private Role role;
    private LocalDate birthday;
}
