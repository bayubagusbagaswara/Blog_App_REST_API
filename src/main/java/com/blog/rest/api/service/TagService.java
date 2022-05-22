package com.blog.rest.api.service;

import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.security.UserPrincipal;

public interface TagService {

    Tag addTag(Tag tag, UserPrincipal currentUser);

}
