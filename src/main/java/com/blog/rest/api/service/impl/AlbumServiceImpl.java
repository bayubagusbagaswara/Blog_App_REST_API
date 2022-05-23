package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.payload.album.AlbumRequest;
import com.blog.rest.api.payload.album.AlbumResponse;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.repository.AlbumRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.AlbumService;
import org.springframework.stereotype.Service;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Album addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public Album getAlbumById(Long id) {
        return null;
    }

    @Override
    public PagedResponse<AlbumResponse> getAllAlbums(int page, int size) {
        return null;
    }

    @Override
    public AlbumResponse updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deleteAlbum(Long id, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public Album getUserAlbums(String username, int page, int size) {
        return null;
    }
}
