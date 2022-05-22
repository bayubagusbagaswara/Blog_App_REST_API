package com.blog.rest.api.service.impl;

import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PhotoService;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Override
    public PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public PhotoResponse getPhotoById(Long id) {
        return null;
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {
        return null;
    }

    @Override
    public PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {
        return null;
    }
}
