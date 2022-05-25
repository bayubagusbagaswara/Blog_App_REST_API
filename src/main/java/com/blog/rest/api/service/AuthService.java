package com.blog.rest.api.service;

import com.blog.rest.api.payload.auth.LoginRequest;
import com.blog.rest.api.payload.jwt.JwtAuthenticationResponse;

public interface AuthService {

    // signin
    JwtAuthenticationResponse signIn(LoginRequest loginRequest);

    // signup
}
