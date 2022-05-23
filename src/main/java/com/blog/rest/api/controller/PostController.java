package com.blog.rest.api.controller;

import com.blog.rest.api.payload.post.PostRequest;
import com.blog.rest.api.payload.post.PostResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> addPost(
            @Valid @RequestBody PostRequest postRequest,
            @CurrentUser UserPrincipal currentUser) {

        PostResponse postResponse = postService.addPost(postRequest, currentUser);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }
}
