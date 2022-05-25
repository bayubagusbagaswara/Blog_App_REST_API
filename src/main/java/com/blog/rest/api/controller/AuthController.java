package com.blog.rest.api.controller;

import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.auth.LoginRequest;
import com.blog.rest.api.payload.auth.SignUpRequest;
import com.blog.rest.api.payload.jwt.JwtAuthenticationResponse;
import com.blog.rest.api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {

        final JwtAuthenticationResponse jwtAuthenticationResponse = authService.signIn(loginRequest);
        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        final User user = authService.signUp(signUpRequest);

        // arahkan ke url /api/users/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
    }

}
