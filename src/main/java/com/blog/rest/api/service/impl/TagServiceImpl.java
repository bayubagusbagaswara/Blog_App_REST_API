package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.repository.TagRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag addTag(Tag tag, UserPrincipal currentUser) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagById(Long id) {
        return null;
    }

    @Override
    public PagedResponse<Tag> getAllTags(int page, int size) {
        return null;
    }

    @Override
    public Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deleteTag(Long id, UserPrincipal currentUser) {
        return null;
    }
}
