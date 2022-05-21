package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CategoryService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public PagedResponse<Category> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        return categoryService.getAllCategories(page, size);
    }

    // get category by id
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(name = "id") Long id) {
        final Category category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    // update category
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody Category category,
            @CurrentUser UserPrincipal currentUser) {

        final Category updatedCategory = categoryService.updateCategory(id, category, currentUser);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

}
