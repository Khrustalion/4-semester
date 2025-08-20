package org.khrustalev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String username;
    private String role;
    private Long userId;
    private Long ownerId;
}
