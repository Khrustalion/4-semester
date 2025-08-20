package org.khrustalev.model.services.dto;

import org.khrustalev.model.entities.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto entity2Dto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getOwner().getId());
    }
}
