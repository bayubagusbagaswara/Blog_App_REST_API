package com.blog.rest.api.service;

import com.blog.rest.api.payload.user.UserIdentityAvailability;
import com.blog.rest.api.payload.user.UserSummary;
import com.blog.rest.api.security.UserPrincipal;

public interface UserService {

    // ambil current user yang sedang mengakses aplikasi kita
    UserSummary getCurrentUser(UserPrincipal currentUser);

    // cek apakah username sudah tersedia atau belum
    UserIdentityAvailability checkUsernameAvailability(String username);
}