package com.blog.rest.api.service;

import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface PhotoService {

    PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

    PhotoResponse getPhotoById(Long id);

    PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

    PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

    ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

    PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);
}
