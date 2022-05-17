package com.blog.rest.api.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserIdentityAvailability {

    private Boolean available;
}
