package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // add category
    @PostMapping
    public ResponseEntity<Category> addCategory(
            @Valid @RequestBody Category category,
            @CurrentUser UserPrincipal currentUser) {

        final Category newCategory = categoryService.addCategory(category, currentUser);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
}
