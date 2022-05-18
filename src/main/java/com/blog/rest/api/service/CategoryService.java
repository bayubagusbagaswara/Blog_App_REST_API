package com.blog.rest.api.service;

import com.blog.rest.api.entity.Category;

public interface CategoryService {

    Category getAllCategories(int page, int size);
}
