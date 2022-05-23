package com.blog.rest.api.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {

    // ambil data UserDetails by id
    UserDetails loadUserById(Long id);
}
