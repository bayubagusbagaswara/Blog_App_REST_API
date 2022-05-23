package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Post;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.payload.post.PostRequest;
import com.blog.rest.api.payload.post.PostResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PostService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(name = "id") Long id) {
        Post post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Post>> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<Post> response = postService.getAllPosts(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Post> updatePost(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody PostRequest newPostRequest,
            @CurrentUser UserPrincipal currentUser) {

        Post post = postService.updatePost(id, newPostRequest, currentUser);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePost(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser) {

        ApiResponse apiResponse = postService.deletePost(id, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<PagedResponse<Post>> getPostsByCategory(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable(name = "id") Long id) {

        PagedResponse<Post> response = postService.getPostsByCategory(id, page, size);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

}
