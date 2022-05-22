package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.entity.Post;
import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.request.PostRequest;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.payload.response.PostResponse;
import com.blog.rest.api.repository.CategoryRepository;
import com.blog.rest.api.repository.PostRepository;
import com.blog.rest.api.repository.TagRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PostService;
import com.blog.rest.api.utils.AppConstants;
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

        // cek user
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, 1L));

        // cek category
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, postRequest.getCategoryId()));

        // bikin object tags
        List<Tag> tags = new ArrayList<>(postRequest.getTags().size());

        // ambil semua tag dari postRequest
        for (String name : postRequest.getTags()) {

            // cek tag by name
            Tag tag = tagRepository.findByName(name);

            // jika name tag nya == null(belum ada), maka simpan tag nya
            // jika sudah ada name tag nya, maka balikan data tag nya
            tag = tag == null ? tagRepository.save(new Tag(name)) : tag;

            // tambahkan tag ke dalam list tags
            tags.add(tag);
        }

        // create post
        Post post = new Post();
        post.setBody(postRequest.getBody());
        post.setTitle(postRequest.getTitle());
        post.setCategory(category);
        post.setUser(user);
        post.setTags(tags);

        // simpan post
        Post newPost = postRepository.save(post);

        // create postRepsonse untuk menyimpan data mapping dari Post ke PostResponse
        PostResponse postResponse = new PostResponse();
        postResponse.setTitle(newPost.getTitle());
        postResponse.setBody(newPost.getBody());
        postResponse.setCategory(newPost.getCategory().getName());

        // buat object tagNames untuk menyimpan data nama tag
        List<String> tagNames = new ArrayList<>(newPost.getTags().size());

        // ambil semua name tag nya, lalu masukkan ke object tagNames
        for (Tag tag : newPost.getTags()) {
            tagNames.add(tag.getName());
        }

        // masukkan data tagNames kedalam property tags milik object PostResponse
        postResponse.setTags(tagNames);

        // balikkan object PostResponse
        return postResponse;
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
    }

    @Override
    public PagedResponse<Post> getAllPosts(int page, int size) {

        // validasi data page and size
        AppUtils.validatePageNumberAndSize(page, size);

        // bikin object pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        // ambil data list of posts
        Page<Post> posts = postRepository.findAll(pageable);

        // ambil content dimana adalah list of post
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

        // cek post
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

        // cek category
        Category category = categoryRepository.findById(newPostRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, newPostRequest.getCategoryId()));

        // cek user apakah memiliki ROLE ADMIN
        if (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            post.setTitle(newPostRequest.getTitle());
            post.setBody(newPostRequest.getBody());
            post.setCategory(category);
            return postRepository.save(post);
        }

        // jika user tidak bisa update post
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this post");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {

        // cek post
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

        // cek user apakah memiliki ROLE_ADMIN
        if (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            postRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You successfully deleted post");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this post");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size) {

        // validasi data page dan size
        AppUtils.validatePageNumberAndSize(page, size);

        // cek user
        User user = userRepository.getUserByName(username);

        // create object pageable
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        // dapatkan object posts
        Page<Post> posts = postRepository.findByCreatedBy(user.getId(), pageable);

        // create object list of post
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
        return null;
    }
}
