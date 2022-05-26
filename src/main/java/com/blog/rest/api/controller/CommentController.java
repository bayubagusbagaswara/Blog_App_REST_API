package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.payload.comment.CommentRequest;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CommentService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addComment(
            @Valid @RequestBody CommentRequest commentRequest,
            @PathVariable(name = "postId") Long postId,
            @CurrentUser UserPrincipal currentUser) {

        Comment newComment = commentService.addComment(commentRequest, postId, currentUser);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id) {

        Comment comment = commentService.getCommentById(postId, id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Comment>> getAllComments(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<Comment> allComments = commentService.getAllComments(postId, page, size);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Comment> updateComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal currentUser) {

        Comment updatedComment = commentService.updateComment(postId, id, commentRequest, currentUser);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser) {

        ApiResponse response = commentService.deleteComment(postId, id, currentUser);
        HttpStatus status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }
}
