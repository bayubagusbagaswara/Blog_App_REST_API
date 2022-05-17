package com.blog.rest.api.payload.user;

import com.blog.rest.api.entity.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

// class payload ini berisi property untuk User
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private Instant joinedAt;

    private String email;

    private Address address;

    private String phone;

    private Long postCount;
}
