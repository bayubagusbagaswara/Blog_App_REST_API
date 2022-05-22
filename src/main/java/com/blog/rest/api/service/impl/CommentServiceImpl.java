package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.entity.Post;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.BlogApiException;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.payload.request.CommentRequest;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.repository.CommentRepository;
import com.blog.rest.api.repository.PostRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private static final String ID_STR = "id";
    private static final String COMMENT_STR = "Comment";
    private static final String POST_STR = "Post";
    private static final String THIS_COMMENT = " this comment";
    private static final String YOU_DON_T_HAVE_PERMISSION_TO = "You don't have permission to ";
    private static final String COMMENT_DOES_NOT_BELONG_TO_POST = "Comment does not belong to post";

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser) {
        // cek post
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));

        // cek user
        final User user = userRepository.getUser(currentUser);

        // create comment
        Comment comment = new Comment(commentRequest.getBody());
        comment.setUser(user);
        comment.setPost(post);
        comment.setName(currentUser.getUsername());
        comment.setEmail(currentUser.getEmail());

        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment getCommentById(Long postId, Long id) {
        // cek post
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));

        // cek comment
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

        if (comment.getPost().getId().equals(post.getId())) {
            return comment;
        }

        // jika comment tidak ditemukan artinya tidak ada comment untuk post tersebut
        throw new BlogApiException(HttpStatus.BAD_REQUEST, COMMENT_DOES_NOT_BELONG_TO_POST);
    }

    @Override
    public PagedResponse<Comment> getAllComments(Long postId, int page, int size) {
        return null;
    }

    @Override
    public Comment updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser) {
        return null;
    }
}
