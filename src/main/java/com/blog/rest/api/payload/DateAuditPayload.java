package com.blog.rest.api.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public abstract class DateAuditPayload {

    private Instant createdAt;

    private Instant updatedAt;

}
