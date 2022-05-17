package com.blog.rest.api.service.impl;

import com.blog.rest.api.payload.user.UserIdentityAvailability;
import com.blog.rest.api.payload.user.UserProfile;
import com.blog.rest.api.payload.user.UserSummary;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return UserSummary.builder()
                .id(currentUser.getId())
                .username(currentUser.getUsername())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .build();
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        return null;
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        return null;
    }

    @Override
    public UserProfile getUserProfile(String username) {
        return null;
    }
}
