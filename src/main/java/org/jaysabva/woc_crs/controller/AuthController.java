package org.jaysabva.woc_crs.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.jaysabva.woc_crs.dto.JwtRequestDto;
import org.jaysabva.woc_crs.entity.Admin;
import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.service.AuthService;
import org.jaysabva.woc_crs.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "APIs for Auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(AuthService authService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JwtRequestDto jwtRequestDto) {
        try {
            Object user = authService.login(jwtRequestDto.username(), jwtRequestDto.password());
            if (user != null) {
                String userId = getUserField(user, "id");
                String userEmail = getUserField(user, "email");
                String role = determineUserRole(user);

                String token = jwtProvider.generateToken(userId, userEmail, role);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.badRequest().body("Invalid Credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    private String getUserField(Object user, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = user.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Allow access to private fields
        return field.get(user).toString();
    }

    private String determineUserRole(Object user) {
        if (user instanceof Admin) {
            return "ADMIN";
        } else if (user instanceof Professor) {
            return "PROFESSOR";
        } else {
            return "STUDENT";
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        try {
            String resultMessage = authService.forgotPassword(email);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody JwtRequestDto password) {
        try {
            String email = jwtProvider.validateToken(token).getSubject();
            String newPassword = password.password();

            String resultMessage = authService.resetPassword(email, newPassword);

            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred: " + e.getMessage());
        }
    }
}
