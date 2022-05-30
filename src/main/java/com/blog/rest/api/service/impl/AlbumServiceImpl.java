package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.exception.BlogApiException;
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
import com.blog.rest.api.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

    private static final String CREATED_AT = "createdAt";

    private static final String ALBUM_STR = "Album";

    private static final String YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION = "You don't have permission to make this operation";

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AlbumServiceImpl(AlbumRepository albumRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Album addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        Album album = new Album();

        modelMapper.map(albumRequest, album);

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
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Album> albums = albumRepository.findAll(pageable);

        if (albums.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), albums.getNumber(), albums.getSize(), albums.getTotalElements(),
                    albums.getTotalPages(), albums.isLast());
        }

        List<AlbumResponse> albumResponses = Arrays.asList(modelMapper.map(albums.getContent(), AlbumResponse[].class));

        return new PagedResponse<>(albumResponses, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(),
                albums.isLast());
    }

    @Override
    public AlbumResponse updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, AppConstants.ID, id));
        User user = userRepository.getUser(currentUser);
        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            album.setTitle(newAlbum.getTitle());
            Album updatedAlbum = albumRepository.save(album);

            AlbumResponse albumResponse = new AlbumResponse();

            modelMapper.map(updatedAlbum, albumResponse);

            return albumResponse;
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
    }

    @Override
    public ApiResponse deleteAlbum(Long id, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, AppConstants.ID, id));
        User user = userRepository.getUser(currentUser);
        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            albumRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You successfully deleted album");
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
    }

    @Override
    public Album getUserAlbums(String username, int page, int size) {
        return null;
    }
}
