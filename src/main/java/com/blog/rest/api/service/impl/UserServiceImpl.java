package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.role.Role;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.entity.user.Address;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.*;
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

    @Override
    public User addUser(User user) {
        // cek apakah user dengan username tertentu ada
        if (userRepository.existsByUsername(user.getUsername())) {
            // jika username sudah ada, maka lempar sebagai exception dan membawa response berikut
            ApiResponse apiResponse = ApiResponse.builder()
                    .success(Boolean.FALSE)
                    .message("Username is already taken")
                    .build();
            throw new BadRequestException(apiResponse);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            // jika email sudah ada
            ApiResponse apiResponse = ApiResponse.builder()
                    .success(Boolean.FALSE)
                    .message("Email is already taken")
                    .build();
            throw new BadRequestException(apiResponse);
        }

        // create roles untuk user baru
        List<Role> roles = new ArrayList<>();
        roles.add(
                roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new AppException("User role not set")));

        // set roles to user
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        // newUser adalah data User baru yang dikirim dari request body, yang isinya adalah data user

        // cari dan dapatkan data user berdasarkan username
        final User user = userRepository.getUserByName(username);

        // cek apakah id user sama dengan id current user
        // atau cek authority current user itu mengandung ROLE_ADMIN
        // karena yang bisa update data user hanyalah user yang role nya adalah ADMIN
        if (user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            // jika true, maka update data user
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            user.setAddress(newUser.getAddress());
            user.setPhone(newUser.getPhone());

            // save user baru
            return userRepository.save(user);
        }

        // jika false, maka kita balikan response gagal mengupdate data user
        ApiResponse apiResponse = ApiResponse.builder()
                .success(Boolean.FALSE)
                .message("You don't have permission to update profile of: " + username)
                .build();
        // lalu lempar error
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        // yang bisa menghapus user hanyalah user yang memiliki ROLE_ADMIN
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));

        // check user dan role user
        if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            // jika user id tidak sama dan tidak memiliki ROLE_ADMIN, maka lempar error
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete profile of: " + username);
            throw new AccessDeniedException(apiResponse);
        }

        // jika user nya sama dan rolenya adalah ADMIN, maka bisa delete
        userRepository.deleteById(user.getId());

        return ApiResponse.builder()
                .success(Boolean.TRUE)
                .message("You successfully deleted profile of: " + username)
                .build();
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        // yang bisa memberikan kan role baru adalah user yang sudah memiliki role admin
        final User user = userRepository.getUserByName(username);

        // buat object roles
        List<Role> roles = new ArrayList<>();
        // masukkan role admin
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("User role not set")));
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User role not set")));

        // set role ke user
        user.setRoles(roles);

        // save user
        userRepository.save(user);

        // balikan response berhasil
        return ApiResponse.builder()
                .success(Boolean.TRUE)
                .message("You gave ADMIN role to user: " + username)
                .build();
    }

    @Override
    public ApiResponse removeAdmin(String username) {
        // remove admin ini sama saja dengan kita buat role baru tapi hanya kita isi role nya sebagai User
        final User user = userRepository.getUserByName(username);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User role not set")));

        user.setRoles(roles);

        userRepository.save(user);

        return ApiResponse.builder()
                .success(Boolean.TRUE)
                .message("You took ADMIN role from user: " + username)
                .build();
    }

    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser, UserInfoRequest userInfoRequest) {
        // yang bisa melakukan set atau update info hanyalah user yang memiliki role ADMIN
        final User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        // create new address
        Address address = Address.builder()
                .street(userInfoRequest.getStreet())
                .suite(userInfoRequest.getSuite())
                .city(userInfoRequest.getCity())
                .zipcode(userInfoRequest.getZipcode())
                .build();

        // cek user apakah memiliki role admin
        if (user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            // jika true, maka update
            user.setAddress(address);
            user.setPhone(userInfoRequest.getPhone());

            // save user
            final User updatedUser = userRepository.save(user);

            final Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

            return UserProfile.builder()
                    .id(updatedUser.getId())
                    .username(updatedUser.getUsername())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .joinedAt(updatedUser.getCreatedAt())
                    .email(updatedUser.getEmail())
                    .address(updatedUser.getAddress())
                    .phone(updatedUser.getPhone())
                    .postCount(postCount)
                    .build();
        }

        // response jika gagal update
        ApiResponse apiResponse = ApiResponse.builder()
                .success(Boolean.FALSE)
                .message("You don't have permission to update users profile")
                .status(HttpStatus.FORBIDDEN)
                .build();

        throw new AccessDeniedException(apiResponse);
    }
}
