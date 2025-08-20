package org.khrustalev.model.services.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.khrustalev.model.entities.users.Role;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private Role role;
    private Long ownerId;

    public UserDto(Long id, String name, String password, Role role, Long ownerId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.ownerId = ownerId;
    }

    public UserDto(Long id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }
}
