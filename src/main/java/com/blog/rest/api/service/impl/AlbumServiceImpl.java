package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.payload.album.AlbumRequest;
import com.blog.rest.api.payload.album.AlbumResponse;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.repository.AlbumRepository;
import com.blog.rest.api.repository.UserRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.AlbumService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AlbumServiceImpl implements AlbumService {

    private static final String CREATED_AT = "createdAt";

    private static final String ALBUM_STR = "Album";

    private static final String YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION = "You don't have permission to make this operation";

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Album addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        Album album = new Album();
        album.setTitle(albumRequest.getTitle());
        album.setPhoto(albumRequest.getPhoto());
        album.setCreatedAt(Instant.now());
        album.setUser(user);

        return albumRepository.save(album);
    }

    @Override
    public Album getAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, AppConstants.ID, id));
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
