package com.blog.rest.api.service;

import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface TagService {

    Tag addTag(Tag tag, UserPrincipal currentUser);

    Tag getTagById(Long id);

    PagedResponse<Tag> getAllTags(int page, int size);

    Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser);

    ApiResponse deleteTag(Long id, UserPrincipal currentUser);

}
