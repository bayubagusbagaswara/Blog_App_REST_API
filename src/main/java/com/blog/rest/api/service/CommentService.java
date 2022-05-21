package com.blog.rest.api.service;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.payload.request.CommentRequest;
import com.blog.rest.api.security.UserPrincipal;

public interface CommentService {

    Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser);
}