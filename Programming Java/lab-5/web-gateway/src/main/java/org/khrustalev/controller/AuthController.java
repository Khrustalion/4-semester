package org.khrustalev.controller;

import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.khrustalev.config.RabbitConfig;
import org.khrustalev.dto.AuthResponseDto;
import org.khrustalev.dto.OwnerDto;
import org.khrustalev.dto.RegisterDto;
import org.khrustalev.dto.UserDto;
import org.khrustalev.entity.User;
import org.khrustalev.exception.UserNotFoundException;
import org.khrustalev.service.BlacklistService;
import org.khrustalev.service.UserService;
import org.khrustalev.util.JwtUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private RabbitTemplate rabbit;
    private UserService userService;
    private BlacklistService blacklistService;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(RabbitTemplate rabbit, UserService userService, BlacklistService blacklistService, JwtUtil jwtUtil) {
        this.rabbit = rabbit;
        this.userService = userService;
        this.blacklistService = blacklistService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody RegisterDto request) {
        OwnerDto ownerReq = new OwnerDto();
        ownerReq.setName(request.getName());
        ownerReq.setBirthday(request.getBirthday());
        Object raw = rabbit.convertSendAndReceive(
                RabbitConfig.OWNER_SAVE_QUEUE,
                ownerReq
        );
        if (raw == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create owner"
            );
        }
        OwnerDto createdOwner = (OwnerDto) raw;

        UserDto newUser = new UserDto(request.getName(), request.getPassword(), request.getRole(), createdOwner.getId());
        UserDto createdUser;
        try {
            createdUser = userService.createUser(newUser);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create user: " + ex.getMessage(),
                    ex
            );
        }

        return jwtUtil.getAuthResponseDto(createdUser);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody UserDto request) {
        UserDto user;

        try {
            user = userService.findById(request.getId());
        }
        catch (UserNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage(),
                    ex);
        }

        if (!request.getPassword().equals(user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials"
            );
        }

        return jwtUtil.getAuthResponseDto(user);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest req) {
        final String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);

            Claims claims = jwtUtil.extractClaims(token);

            String jti = claims.getId();

            blacklistService.revoke(jti);
        }
    }
}
