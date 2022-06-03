package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.entity.Photo;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.repository.AlbumRepository;
import com.blog.rest.api.repository.PhotoRepository;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PhotoService;
import com.blog.rest.api.utils.AppConstants;
import com.blog.rest.api.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.blog.rest.api.utils.AppConstants.*;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, AlbumRepository albumRepository) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
    }

    @Override
    public PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser) {

        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));

        if (album.getUser().getId().equals(currentUser.getId())) {
            Photo photo = new Photo(photoRequest.getTitle(), photoRequest.getUrl(), photoRequest.getThumbnailUrl(), album);

            Photo newPhoto = photoRepository.save(photo);
            return new PhotoResponse(newPhoto.getId(), newPhoto.getTitle(), newPhoto.getUrl(), newPhoto.getThumbnailUrl(), newPhoto.getAlbum().getId());
        }

        throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission to add photo in this album"));
    }

    @Override
    public PhotoResponse getPhotoById(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        return new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(), photo.getAlbum().getId());
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Photo> photos = photoRepository.findAll(pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());

        for (Photo photo : photos.getContent()) {
            PhotoResponse photoResponse = new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(), photo.getAlbum().getId());
            photoResponses.add(photoResponse);
        }

        if (photos.getNumberOfElements() == 0) {
            return PagedResponse.<PhotoResponse>builder()
                    .content(Collections.emptyList())
                    .page(photos.getNumber())
                    .size(photos.getSize())
                    .totalElements(photos.getTotalElements())
                    .totalPages(photos.getTotalPages())
                    .last(photos.isLast())
                    .build();
        }

        return PagedResponse.<PhotoResponse>builder()
                .content(photoResponses)
                .page(photos.getNumber())
                .size(photos.getSize())
                .totalElements(photos.getTotalElements())
                .totalPages(photos.getTotalPages())
                .last(photos.isLast())
                .build();
    }

    @Override
    public PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser) {

        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));

        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            photo.setTitle(photoRequest.getTitle());
            photo.setThumbnailUrl(photoRequest.getThumbnailUrl());
            photo.setAlbum(album);

            Photo updatedPhoto = photoRepository.save(photo);

            return new PhotoResponse(updatedPhoto.getId(), updatedPhoto.getTitle(), updatedPhoto.getUrl(), updatedPhoto.getThumbnailUrl(), updatedPhoto.getAlbum().getId());
        }

        throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission to update this photo"));
    }

    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            photoRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Photo deleted successfully");
        }

        throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo"));
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Photo> photos = photoRepository.findByAlbumId(albumId, pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());

        for (Photo photo : photos.getContent()) {
            PhotoResponse photoResponse = new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(), photo.getAlbum().getId());
            photoResponses.add(photoResponse);
        }

        return PagedResponse.<PhotoResponse>builder()
                .content(photoResponses)
                .page(photos.getNumber())
                .size(photos.getSize())
                .totalElements(photos.getTotalElements())
                .totalPages(photos.getTotalPages())
                .last(photos.isLast())
                .build();
    }

}
