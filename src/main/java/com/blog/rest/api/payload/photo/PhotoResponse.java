package com.blog.rest.api.payload.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponse {

    private Long id;

    private String title;

    private String url;

    private String thumbnailUrl;

    private Long albumId;

}
