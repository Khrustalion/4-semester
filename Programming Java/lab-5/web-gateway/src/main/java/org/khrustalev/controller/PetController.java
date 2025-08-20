package org.khrustalev.controller;

import org.khrustalev.dto.AddFriendDto;
import org.khrustalev.dto.OwnerDto;
import org.khrustalev.dto.Page.PageDto;
import org.khrustalev.dto.Page.PageResponseDto;
import org.khrustalev.dto.PetDto;
import org.khrustalev.config.RabbitConfig;
import org.khrustalev.dto.UserDto;
import org.khrustalev.entity.User;
import org.khrustalev.service.UserService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final RabbitTemplate rabbit;
    private final UserService userService;

    public PetController(RabbitTemplate rabbit, UserService userService) {
        this.rabbit = rabbit;
        this.userService = userService;
    }

    private Throwable unwrap(Throwable t) {
        Throwable c = t.getCause();
        return (c == null || c == t) ? t : unwrap(c);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public PetDto create(@RequestBody PetDto dto) {
        try {
            Object owner = rabbit.convertSendAndReceive(RabbitConfig.OWNER_GET_QUEUE, dto.getOwnerId());
            if (owner == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
            }

            OwnerDto ownerDto = (OwnerDto) owner;

            Object raw = rabbit.convertSendAndReceive(RabbitConfig.PET_SAVE_QUEUE, dto);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Empty response from pet-service");
            }

            ownerDto.addPet(dto.getId());

            rabbit.convertAndSend(RabbitConfig.OWNER_SAVE_QUEUE, ownerDto);

            return (PetDto) raw;
        } catch (AmqpException ex) {
            Throwable root = unwrap(ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    root.getMessage(),
                    ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/update")
    public PetDto update(@RequestBody PetDto dto, Authentication authentication) {
        try {
            User userDto = (User) authentication.getPrincipal();
            UserDto user = userService.findById(userDto.getId());

            if (user == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found");
            }

            PetDto req = new PetDto();
            req.setId(dto.getId());

            Object raw = rabbit.convertSendAndReceive(RabbitConfig.PET_GET_QUEUE, req);

            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found");
            }

            PetDto pet = (PetDto) raw;

            if (!user.getOwnerId().equals(pet.getOwnerId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Forbidden act");
            }

            raw = rabbit.convertSendAndReceive(RabbitConfig.PET_UPDATE_QUEUE, dto);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found");
            }
            return (PetDto) raw;
        } catch (AmqpException ex) {
            Throwable root = unwrap(ex);
            if (root instanceof RuntimeException) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        root.getMessage(),
                        ex);
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    root.getMessage(),
                    ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public PetDto getById(@PathVariable Long id) {
        PetDto req = new PetDto();
        req.setId(id);
        try {
            Object raw = rabbit.convertSendAndReceive(RabbitConfig.PET_GET_QUEUE, req);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found");
            }
            return (PetDto) raw;
        } catch (AmqpException ex) {
            Throwable root = unwrap(ex);
            if (root instanceof RuntimeException) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        root.getMessage(),
                        ex);
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    root.getMessage(),
                    ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/all")
    public PageResponseDto<PetDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean descending
    ) {
        PageDto pd = new PageDto(page, size, sortBy, descending);
        try {
            Object raw = rabbit.convertSendAndReceive(RabbitConfig.PET_PAGE_QUEUE, pd);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Empty response from pet-service");
            }
            @SuppressWarnings("unchecked")
            PageResponseDto<PetDto> pageResult = (PageResponseDto<PetDto>) raw;
            return pageResult;
        } catch (AmqpException ex) {
            Throwable root = unwrap(ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    root.getMessage(),
                    ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public PetDto delete(@PathVariable Long id, Authentication authentication) {
        try {
            User userDto = (User) authentication.getPrincipal();
            UserDto user = userService.findById(userDto.getId());

            if (user == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found");
            }

            PetDto req = new PetDto();
            req.setId(id);

            Object raw = rabbit.convertSendAndReceive(RabbitConfig.PET_GET_QUEUE, req);

            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found");
            }

            PetDto pet = (PetDto) raw;

            if (!user.getOwnerId().equals(pet.getOwnerId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Forbidden act");
            }

            raw = rabbit.convertSendAndReceive(RabbitConfig.PET_DELETE_QUEUE, req);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found");
            }
            return (PetDto) raw;
        } catch (AmqpException ex) {
            Throwable root = unwrap(ex);
            if (root instanceof RuntimeException) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        root.getMessage(),
                        ex);
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    root.getMessage(),
                    ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{petId}/add-friend/{friendId}")
    public PetDto addFriend(
            @PathVariable Long petId,
            @PathVariable Long friendId,
            Authentication authentication
    ) {
        try {
            User userDto = (User) authentication.getPrincipal();
            UserDto user = userService.findById(userDto.getId());

            if (user == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found");
            }

            PetDto p = new PetDto(); p.setId(petId);
            PetDto f = new PetDto(); f.setId(friendId);
            AddFriendDto requestDto = new AddFriendDto(p, f);

            Object rawPet = rabbit.convertSendAndReceive(RabbitConfig.PET_GET_QUEUE, p);
            Object rawFriend = rabbit.convertSendAndReceive(RabbitConfig.PET_GET_QUEUE, f);

            if (rawPet == null || rawFriend == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found");
            }

            PetDto pet = (PetDto) rawPet;

            System.out.println(pet.getOwnerId() + " " + user.getOwnerId());

            if (!user.getOwnerId().equals(pet.getOwnerId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Forbidden act");
            }

            Object raw = rabbit.convertSendAndReceive(RabbitConfig.PET_ADD_FRIEND_QUEUE, requestDto);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Empty response from pet-service");
            }
            return (PetDto) raw;
        } catch (AmqpException ex) {
            Throwable root = unwrap(ex);
            if (root instanceof RuntimeException) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        root.getMessage(),
                        ex);
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    root.getMessage(),
                    ex);
        }
    }
}
