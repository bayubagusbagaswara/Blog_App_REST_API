package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.repository.TagRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.TagService;
import com.blog.rest.api.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        return tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
    }

    @Override
    public PagedResponse<Tag> getAllTags(int page, int size) {
        // validasi data page dan size
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Tag> tags = tagRepository.findAll(pageable);

        List<Tag> content = tags.getNumberOfElements() == 0 ? Collections.emptyList() : tags.getContent();

        return PagedResponse.<Tag>builder()
                .content(content)
                .page(tags.getNumber())
                .size(tags.getSize())
                .totalPages(tags.getTotalPages())
                .totalElements(tags.getTotalElements())
                .last(tags.isLast())
                .build();
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
