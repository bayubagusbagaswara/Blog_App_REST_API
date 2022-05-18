package com.blog.rest.api.service;

import com.blog.rest.api.entity.Category;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface CategoryService {

    Category getAllCategories(int page, int size);

    Category getCategoryById(Long id);

    Category addCategory(Category category, UserPrincipal currentUser);

    Category updateCategory(Long id, Category newCategory, UserPrincipal userPrincipal) throws UnauthorizedException;

    ApiResponse deleteCategory(Long id, UserPrincipal currentUser) throws UnauthorizedException;
}
