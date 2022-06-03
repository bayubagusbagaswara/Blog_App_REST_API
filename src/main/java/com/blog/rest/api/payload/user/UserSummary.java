package com.blog.rest.api.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSummary {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
