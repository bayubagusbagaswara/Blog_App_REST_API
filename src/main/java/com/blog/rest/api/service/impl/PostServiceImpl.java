package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.entity.Post;
import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.ResourceNotFoundException;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return null;
    }

    @Override
    public Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<Post> getPostsByCategory(Long id, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<Post> getPostsByTag(Long id, int page, int size) {
        return null;
    }
}
