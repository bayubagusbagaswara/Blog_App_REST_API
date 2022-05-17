package com.blog.rest.api.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class UserDateAuditPayload extends DateAuditPayload {

    private String createdBy;

    private String updatedBy;
}
