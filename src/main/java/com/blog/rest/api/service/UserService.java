package com.blog.rest.api.service;

import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.payload.user.UserIdentityAvailability;
import com.blog.rest.api.payload.user.UserProfile;
import com.blog.rest.api.payload.user.UserSummary;
import com.blog.rest.api.security.UserPrincipal;

public interface UserService {

    // ambil current user yang sedang mengakses aplikasi kita
    UserSummary getCurrentUser(UserPrincipal currentUser);

    // cek apakah username sudah tersedia atau belum
    UserIdentityAvailability checkUsernameAvailability(String username);

    // cek apakah email sudah terdaftar atau belum
    UserIdentityAvailability checkEmailAvailability(String email);

    // ambil profile user
    UserProfile getUserProfile(String username);

    // menambahkan user baru
    User addUser(User user);

    // update user
    User updateUser(User newUser, String username, UserPrincipal currentUser);
}
