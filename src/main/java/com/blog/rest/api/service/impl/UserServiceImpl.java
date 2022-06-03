package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.role.Role;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.entity.user.Address;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.AccessDeniedException;
import com.blog.rest.api.exception.AppException;
import com.blog.rest.api.exception.BadRequestException;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.user.UserInfoRequest;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.user.UserIdentityAvailability;
import com.blog.rest.api.payload.user.UserProfile;
import com.blog.rest.api.payload.user.UserSummary;
import com.blog.rest.api.repository.PostRepository;
import com.blog.rest.api.repository.RoleRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ROLE_NOT_SET = "User role not set";
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
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName());
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        final Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserProfile getUserProfile(String username) {
        final User user = userRepository.getUserByName(username);
        final Long postCount = postRepository.countByCreatedBy(user.getId());
        return new UserProfile(
                user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getCreatedAt(), user.getEmail(), user.getAddress(), user.getPhone(), postCount);
    }

    @Override
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException(new ApiResponse(Boolean.FALSE, "Username is already taken"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException(new ApiResponse(Boolean.FALSE, "Email is already taken"));
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        final User user = userRepository.getUserByName(username);

        if (user.getId().equals(currentUser.getId()) ||
                currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            user.setAddress(newUser.getAddress());
            user.setPhone(newUser.getPhone());
            return userRepository.save(user);
        }

        throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username));
    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        final User user = userRepository.getUserByName(username);

        if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            throw new AccessDeniedException(new ApiResponse(Boolean.FALSE, "You don't have permission to delete profile of: " + username));
        }

        userRepository.deleteById(user.getId());
        return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        final User user = userRepository.getUserByName(username);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));

        user.setRoles(roles);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);
    }

    @Override
    public ApiResponse removeAdmin(String username) {
        final User user = userRepository.getUserByName(username);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));

        user.setRoles(roles);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);
    }

    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser, UserInfoRequest userInfoRequest) {
        final User user = userRepository.getUserByName(currentUser.getUsername());

        Address address = new Address(userInfoRequest.getStreet(), userInfoRequest.getSuite(), userInfoRequest.getCity(), userInfoRequest.getZipcode());

        if (user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setAddress(address);
            user.setPhone(userInfoRequest.getPhone());
            final User updatedUser = userRepository.save(user);
            final Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

            return new UserProfile(
                    updatedUser.getId(), updatedUser.getUsername(), updatedUser.getFirstName(), updatedUser.getLastName(),
                    updatedUser.getCreatedAt(), updatedUser.getEmail(), updatedUser.getAddress(), updatedUser.getPhone(), postCount);
        }

        throw new AccessDeniedException(new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile", HttpStatus.FORBIDDEN));
    }
}
