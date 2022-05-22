package com.blog.rest.api.service;

import com.blog.rest.api.entity.Post;
import com.blog.rest.api.payload.request.PostRequest;
import com.blog.rest.api.payload.response.PostResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface PostService {

    PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

    Post getPostById(Long id);

}
