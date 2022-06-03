package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.entity.Post;
import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.post.PostRequest;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.payload.post.PostResponse;
import com.blog.rest.api.repository.CategoryRepository;
import com.blog.rest.api.repository.PostRepository;
import com.blog.rest.api.repository.TagRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PostService;
import com.blog.rest.api.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.blog.rest.api.utils.AppConstants.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, currentUser.getId()));

        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, postRequest.getCategoryId()));

        List<Tag> tags = new ArrayList<>(postRequest.getTags().size());

        for (String name : postRequest.getTags()) {

            Tag tag = tagRepository.findByName(name);

            tag = tag == null ? tagRepository.save(new Tag(name)) : tag;

            tags.add(tag);
        }

        Post post = new Post(postRequest.getTitle(), postRequest.getBody(), user, category, tags);
        Post newPost = postRepository.save(post);

        List<String> tagNames = new ArrayList<>(newPost.getTags().size());
        for (Tag tag : newPost.getTags()) {
            tagNames.add(tag.getName());
        }

        return new PostResponse(newPost.getTitle(), newPost.getBody(), newPost.getCategory().getName(), tagNames);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
    }

    @Override
    public PagedResponse<Post> getAllPosts(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return PagedResponse.<Post>builder()
                .content(content)
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .last(posts.isLast())
                .build();
    }

    @Override
    public Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

        Category category = categoryRepository.findById(newPostRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, newPostRequest.getCategoryId()));

        if (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            post.setTitle(newPostRequest.getTitle());
            post.setBody(newPostRequest.getBody());
            post.setCategory(category);
            return postRepository.save(post);
        }

        throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission to edit this post"));
    }

    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

        if (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            postRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You successfully deleted post");
        }

        throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission to delete this post"));
    }

    @Override
    public PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        User user = userRepository.getUserByName(username);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findByCreatedBy(user.getId(), pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return PagedResponse.<Post>builder()
                .content(content)
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    @Override
    public PagedResponse<Post> getPostsByCategory(Long id, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findByCategory(category.getId(), pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return PagedResponse.<Post>builder()
                .content(content)
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    @Override
    public PagedResponse<Post> getPostsByTag(Long id, int page, int size) {

//        AppUtils.validatePageNumberAndSize(page, size);
//
//        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TAG, ID, id));
//
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
//
//        Page<Post> posts = postRepository.findByTags(Collections.singletonList(tag), pageable);
//
//        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();
//
//        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
//                posts.getTotalPages(), posts.isLast());
        return null;
    }
}
