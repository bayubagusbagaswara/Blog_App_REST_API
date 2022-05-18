package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.repository.CategoryRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CategoryService;
import com.blog.rest.api.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        // validasi dulu
        AppUtils.validatePageNumberAndSize(page, size);

        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        final Page<Category> categories = categoryRepository.findAll(pageable);

        // jika categories tidak ada element atau == 0, maka balikan list kosong
        // dan jika ada datanya, maka ambil content nya
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
        return null;
    }

    @Override
    public Category addCategory(Category category, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public Category updateCategory(Long id, Category newCategory, UserPrincipal userPrincipal) throws UnauthorizedException {
        return null;
    }

    @Override
    public ApiResponse deleteCategory(Long id, UserPrincipal currentUser) throws UnauthorizedException {
        return null;
    }
}
