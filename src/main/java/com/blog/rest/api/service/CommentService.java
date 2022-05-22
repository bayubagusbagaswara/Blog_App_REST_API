package com.blog.rest.api.service;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.payload.request.CommentRequest;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface CommentService {

    Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser);

    Comment getCommentById(Long postId, Long id);

    PagedResponse<Comment> getAllComments(Long postId, int page, int size);

    Comment updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser);

    ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);

}
