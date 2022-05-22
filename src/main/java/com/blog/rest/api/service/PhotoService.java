package com.blog.rest.api.service;

import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface PhotoService {

    PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

    PhotoResponse getPhotoById(Long id);

    PagedResponse<PhotoResponse> getAllPhotos(int page, int size);
}