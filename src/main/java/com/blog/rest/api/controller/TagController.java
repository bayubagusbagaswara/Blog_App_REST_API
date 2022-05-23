package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Tag;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.TagService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Tag> addTag(
            @Valid @RequestBody Tag tag,
            @CurrentUser UserPrincipal currentUser) {

        Tag newTag = tagService.addTag(tag, currentUser);
        return new ResponseEntity<>(newTag, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable(name = "id") Long id) {
        Tag tag = tagService.getTagById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Tag>> getAllTags(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<Tag> response = tagService.getAllTags(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Tag> updateTag(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody Tag tag,
            @CurrentUser UserPrincipal currentUser) {

        Tag updatedTag = tagService.updateTag(id, tag, currentUser);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

}
