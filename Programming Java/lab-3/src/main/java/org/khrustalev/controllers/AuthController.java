package org.khrustalev.controllers;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.khrustalev.configs.JwtUtil;
import org.khrustalev.model.services.OwnerService;
import org.khrustalev.model.services.UserService;
import org.khrustalev.model.services.dto.OwnerDto;
import org.khrustalev.model.services.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserService userService;
    private final OwnerService ownerService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, OwnerService ownerService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.ownerService = ownerService;
        this.jwtUtil = jwtUtil;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String username;
        private String role;
        private Long userId;

        public AuthResponse(String token, String username, String role, Long userId) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.userId = userId;
        }
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody UserDto request) {
        OwnerDto OwnerDto = new OwnerDto();
        OwnerDto.setName(request.getName());
        OwnerDto returnOwner = ownerService.save(OwnerDto);
        request.setOwnerId(returnOwner.getId());
        UserDto user = userService.createUser(request);

        return getAuthResponse(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserDto request) {
        UserDto user = userService.findById(request.getId());

        if (user != null && user.getPassword().equals(request.getPassword())) {
            return getAuthResponse(user);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully. Please remove the token from client storage.");
    }

    @NotNull
    private AuthController.AuthResponse getAuthResponse(UserDto user) {
        String token = null;
        try {
            token = Jwts.builder()
                    .setSubject(user.getName())
                    .claim("userId", user.getId())
                    .claim("role", user.getRole().name())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getExpiration()))
                    .signWith(jwtUtil.getSecret())
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AuthResponse(token, user.getName(), user.getRole().name(), user.getId());
    }

    @GetMapping("/all")
    public Page<UserDto> getAllOwnersBySortingPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean descending) {
        return this.userService.getOwnersBySortingPaging(page, size, sortBy, descending);
    }
}

