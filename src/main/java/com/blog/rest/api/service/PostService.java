package com.blog.rest.api.service;

import com.blog.rest.api.entity.Post;
import com.blog.rest.api.payload.request.PostRequest;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.payload.response.PostResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface PostService {

    PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

    Post getPostById(Long id);

    PagedResponse<Post> getAllPosts(int page, int size);

    Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal userPrincipal);
}
