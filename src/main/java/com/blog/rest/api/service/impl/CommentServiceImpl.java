package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.entity.Post;
import com.blog.rest.api.entity.role.RoleName;
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
import com.blog.rest.api.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        // validasi dulu data page dan size
        AppUtils.validatePageNumberAndSize(page, size);

        // buat object pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // cari comment by post id, masukkan parameter postId dan pageable
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        return PagedResponse.<Comment>builder()
                .content(comments.getContent())
                .page(comments.getNumber())
                .size(comments.getSize())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    @Override
    public Comment updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser) {

        // cek post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));

        // cek comment
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

        // cek apakah post dari comment dan post itu sama, jika tidak sama, maka lempar exception
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, COMMENT_DOES_NOT_BELONG_TO_POST);
        }

        // cek apakah user nya sama atau user yang memiliki ROLE ADMIN
        // jika sama, maka simpan comment nya
        if (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            comment.setBody(commentRequest.getBody());
            return commentRepository.save(comment);
        }

        // tidak bisa update comment, maka lempar exception
        throw new BlogApiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO + "update" + THIS_COMMENT);
    }

    @Override
    public ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser) {
        // cek post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));

        // cek comment
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

        // cek post apakah sama atau tidak
        if (!comment.getPost().getId().equals(post.getId())) {
            return new ApiResponse(Boolean.FALSE, COMMENT_DOES_NOT_BELONG_TO_POST);
        }

        // cek user apakah sama atau user memiliki ROLE ADMIN
        if (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            commentRepository.deleteById(comment.getId());
            return new ApiResponse(Boolean.TRUE, "You successfully deleted comment");
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO + "delete" + THIS_COMMENT);
    }
}
