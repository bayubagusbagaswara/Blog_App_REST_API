package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Post;
import com.blog.rest.api.payload.request.PostRequest;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.payload.response.PostResponse;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public Post getPostById(Long id) {
        return null;
    }

    @Override
    public PagedResponse<Post> getAllPosts(int page, int size) {
        return null;
    }

    @Override
    public Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<Post> getPostsByCategory(Long id, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<Post> getPostsByTag(Long id, int page, int size) {
        return null;
    }
}
