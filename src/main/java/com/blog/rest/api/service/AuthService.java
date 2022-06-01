package com.blog.rest.api.service;

import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.payload.auth.LoginRequest;
import com.blog.rest.api.payload.auth.SignUpRequest;
import com.blog.rest.api.payload.jwt.JwtAuthenticationResponse;

public interface AuthService {

    JwtAuthenticationResponse signIn(LoginRequest loginRequest);

    User signUp(SignUpRequest signUpRequest);
}
