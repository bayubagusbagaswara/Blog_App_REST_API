package com.blog.rest.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    // ambil data UserDetails by id
    UserDetails loadUserById(Long id);
}
