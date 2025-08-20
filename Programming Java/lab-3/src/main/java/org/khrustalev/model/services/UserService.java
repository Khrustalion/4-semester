package org.khrustalev.model.services;

import org.khrustalev.model.entities.owners.Owner;
import org.khrustalev.model.entities.users.User;
import org.khrustalev.model.services.dto.OwnerDto;
import org.khrustalev.model.services.dto.UserDto;
import org.khrustalev.model.services.dto.UserMapper;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.khrustalev.repositories.OwnerRepository;
import org.khrustalev.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, OwnerRepository ownerRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setOwner(ownerRepository.getReferenceById(userDto.getOwnerId()));

        return userMapper.entity2Dto(userRepository.save(user));
    }

    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException("User with id " + id + " does not exist"));

        return userMapper.entity2Dto(user);
    }

    public Page<UserDto> getOwnersBySortingPaging(int page, int size, String sortBy, boolean descending) {
        Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> usersPage = this.userRepository.findAll(pageable);

        return usersPage.map(this.userMapper::entity2Dto);
    }
}
