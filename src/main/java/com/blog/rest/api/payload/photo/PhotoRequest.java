package com.blog.rest.api.payload.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoRequest {

    @NotBlank
    @Size(min = 3)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String url;

    @NotBlank
    @Size(min = 10)
    private String thumbnailUrl;

    @NotNull
    private Long albumId;

}
