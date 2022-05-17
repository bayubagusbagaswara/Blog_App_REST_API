package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.payload.user.UserIdentityAvailability;
import com.blog.rest.api.payload.user.UserProfile;
import com.blog.rest.api.payload.user.UserSummary;
import com.blog.rest.api.repository.PostRepository;
import com.blog.rest.api.repository.RoleRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        // jika username sudah ada, maka kita balikan false
        // harapanya dengan nilai false ini, maka tidak bisa memasukkan username dengan data yang sama
        final Boolean isAvailable = !userRepository.existsByUsername(username);
        return UserIdentityAvailability.builder()
                .available(isAvailable)
                .build();
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return UserIdentityAvailability.builder()
                .available(isAvailable)
                .build();
    }

    @Override
    public UserProfile getUserProfile(String username) {
        final User user = userRepository.getUserByName(username);
        final Long postCount = postRepository.countByCreatedBy(user.getId());
        return UserProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .joinedAt(user.getCreatedAt())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .postCount(postCount)
                .build();
    }
}
