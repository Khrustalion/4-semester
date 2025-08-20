package org.khrustalev.controller;

import org.khrustalev.dto.OwnerDto;
import org.khrustalev.dto.Page.PageDto;
import org.khrustalev.config.RabbitConfig;
import org.khrustalev.dto.Page.PageResponseDto;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final RabbitTemplate rabbit;

    public OwnerController(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    private Throwable unwrap(Throwable t) {
        Throwable c = t.getCause();
        return (c == null || c == t) ? t : unwrap(c);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerDto create(@RequestBody OwnerDto dto) {
        try {
            Object raw = rabbit.convertSendAndReceive(RabbitConfig.OWNER_SAVE_QUEUE, dto);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Empty response from owner-service");
            }
            return (OwnerDto) raw;
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
    public OwnerDto update(@RequestBody OwnerDto dto) {
        try {
            Object raw = rabbit.convertSendAndReceive(RabbitConfig.OWNER_UPDATE_QUEUE, dto);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found");
            }
            return (OwnerDto) raw;
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
    public OwnerDto getById(@PathVariable Long id) {
        OwnerDto req = new OwnerDto();
        req.setId(id);
        try {
            Object raw = rabbit.convertSendAndReceive(RabbitConfig.OWNER_GET_QUEUE, req);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found");
            }
            return (OwnerDto) raw;
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
    public PageResponseDto<OwnerDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean descending
    ) {
        PageDto pd = new PageDto(page, size, sortBy, descending);

        try {
            Object raw = rabbit.convertSendAndReceive(
                    RabbitConfig.OWNER_PAGE_QUEUE,
                    pd
            );
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Empty response from owner-service");
            }
            @SuppressWarnings("unchecked")
            PageResponseDto<OwnerDto> result = (PageResponseDto<OwnerDto>) raw;
            return result;
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
    public OwnerDto delete(@PathVariable Long id) {
        OwnerDto req = new OwnerDto();
        req.setId(id);
        try {
            Object raw = rabbit.convertSendAndReceive(RabbitConfig.OWNER_DELETE_QUEUE, req);
            if (raw == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found");
            }
            return (OwnerDto) raw;
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
