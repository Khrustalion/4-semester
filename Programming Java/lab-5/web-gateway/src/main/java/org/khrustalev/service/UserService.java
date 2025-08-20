package org.khrustalev.service;

import org.khrustalev.dto.UserDto;
import org.khrustalev.entity.User;
import org.khrustalev.exception.UserNotFoundException;
import org.khrustalev.mapper.UserMapper;
import org.khrustalev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setOwnerId(userDto.getOwnerId());

        return userMapper.entity2Dto(userRepository.save(user));
    }

    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exist"));

        return userMapper.entity2Dto(user);
    }

    public Page<UserDto> getOwnersBySortingPaging(int page, int size, String sortBy, boolean descending) {
        Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> usersPage = this.userRepository.findAll(pageable);

        return usersPage.map(this.userMapper::entity2Dto);
    }
}
