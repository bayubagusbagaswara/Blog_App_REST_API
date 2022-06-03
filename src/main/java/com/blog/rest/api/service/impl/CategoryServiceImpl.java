package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.repository.CategoryRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CategoryService;
import com.blog.rest.api.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PagedResponse<Category> getAllCategories(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        final Page<Category> categories = categoryRepository.findAll(pageable);

        List<Category> content = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();

        return PagedResponse.<Category>builder()
                .content(content)
                .page(categories.getNumber())
                .size(categories.getSize())
                .totalElements(categories.getTotalElements())
                .totalPages(categories.getTotalPages())
                .last(categories.isLast())
                .build();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public Category addCategory(Category category, UserPrincipal currentUser) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category newCategory, UserPrincipal currentUser) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (category.getCreatedBy().equals(currentUser.getUsername()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            category.setName(newCategory.getName());
            return categoryRepository.save(category);
        }

        throw new UnauthorizedException("You don't have permission to edit this category");
    }

    @Override
    public ApiResponse deleteCategory(Long id, UserPrincipal currentUser) throws UnauthorizedException {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (category.getCreatedBy().equals(currentUser.getUsername()) ||
                currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            categoryRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You successfully deleted category", HttpStatus.OK);
        }

        throw new UnauthorizedException("You don't have permission to delete this category");
    }
}
