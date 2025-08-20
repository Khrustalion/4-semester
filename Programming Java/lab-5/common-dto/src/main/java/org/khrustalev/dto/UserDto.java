package org.khrustalev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private Role role;
    private Long ownerId;

    public UserDto(String name, String password, Role role, Long ownerId) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.ownerId = ownerId;
    }
}
