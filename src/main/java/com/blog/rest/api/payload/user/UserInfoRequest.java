package com.blog.rest.api.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {

    @NotBlank(message = "Street must not be blank")
    private String street;

    @NotBlank(message = "Suite must not be blank")
    private String suite;

    @NotBlank(message = "City must not be blank")
    private String city;

    @NotBlank(message = "Zipcode must not be blank")
    private String zipcode;

    private String phone;
}
