package com.blog.rest.api.service;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.payload.album.AlbumRequest;
import com.blog.rest.api.payload.album.AlbumResponse;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.security.UserPrincipal;

public interface AlbumService {

    Album addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser);

    Album getAlbumById(Long id);

    PagedResponse<AlbumResponse> getAllAlbums(int page, int size);

    AlbumResponse updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser);

    ApiResponse deleteAlbum(Long id, UserPrincipal currentUser);

    Album getUserAlbums(String username, int page, int size);
}
