package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.payload.comment.CommentRequest;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addComment(
            @Valid @RequestBody CommentRequest commentRequest,
            @PathVariable(name = "postId") Long postId,
            @CurrentUser UserPrincipal currentUser) {

        Comment newComment = commentService.addComment(commentRequest, postId, currentUser);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
}
