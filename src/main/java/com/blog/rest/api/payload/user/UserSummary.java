package com.blog.rest.api.payload.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// class payload ini berisi data atau property User
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummary {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
