package org.khrustalev.mapper;

import org.khrustalev.dto.UserDto;
import org.khrustalev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto entity2Dto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getOwnerId());
    }
}
