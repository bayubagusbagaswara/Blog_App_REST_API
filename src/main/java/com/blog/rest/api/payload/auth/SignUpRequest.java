package com.blog.rest.api.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Firstname must not be blank")
    @Size(min = 4, max = 40)
    private String firstName;

    @NotBlank(message = "Lastname must not be blank")
    @Size(min = 4, max = 40)
    private String lastName;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be formatted")
    @Size(max = 40)
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 20)
    private String password;
}
