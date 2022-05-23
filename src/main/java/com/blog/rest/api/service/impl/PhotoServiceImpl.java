package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.entity.Photo;
import com.blog.rest.api.entity.role.RoleName;
import com.blog.rest.api.exception.ResourceNotFoundException;
import com.blog.rest.api.exception.UnauthorizedException;
import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.payload.response.ApiResponse;
import com.blog.rest.api.payload.response.PagedResponse;
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

        // cek album
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));

        // cek user by id apakah sama
        if (album.getUser().getId().equals(currentUser.getId())) {

            // create photo
            Photo photo = Photo.builder()
                    .title(photoRequest.getTitle())
                    .url(photoRequest.getUrl())
                    .thumbnailUrl(photoRequest.getThumbnailUrl())
                    .album(album)
                    .build();

            // save photo
            Photo newPhoto = photoRepository.save(photo);

            return PhotoResponse.builder()
                    .id(newPhoto.getId())
                    .title(newPhoto.getTitle())
                    .url(newPhoto.getUrl())
                    .thumbnailUrl(newPhoto.getThumbnailUrl())
                    .albumId(newPhoto.getAlbum().getId())
                    .build();
        }

        // balikan jika tidak bisa menambahkan photo
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add photo in this album");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PhotoResponse getPhotoById(Long id) {
        // cari photo
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        return PhotoResponse.builder()
                .id(photo.getId())
                .title(photo.getTitle())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .albumId(photo.getAlbum().getId())
                .build();
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {

        // validasi data page dan size
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Photo> photos = photoRepository.findAll(pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());

        for (Photo photo : photos.getContent()) {
            PhotoResponse photoResponse = PhotoResponse.builder()
                    .id(photo.getId())
                    .title(photo.getTitle())
                    .url(photo.getUrl())
                    .thumbnailUrl(photo.getThumbnailUrl())
                    .albumId(photo.getAlbum().getId())
                    .build();
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

        // cek album
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));

        // cek photo
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        // cek apakah user di album sama dengan current user
        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            // update photo
            photo.setTitle(photoRequest.getTitle());
            photo.setThumbnailUrl(photoRequest.getThumbnailUrl());
            photo.setAlbum(album);

            // simpan photo
            Photo updatedPhoto = photoRepository.save(photo);

            // balikan data photo yang berhasil di update
            return PhotoResponse.builder()
                    .id(updatedPhoto.getId())
                    .title(updatedPhoto.getTitle())
                    .url(updatedPhoto.getUrl())
                    .thumbnailUrl(updatedPhoto.getThumbnailUrl())
                    .albumId(updatedPhoto.getAlbum().getId())
                    .build();
        }

        // response jika tidak berhasil update photo
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update this photo");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {
        // get photo
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        // cek apakah user photo sama dengan current user
        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            // hapus photo
            photoRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Photo deleted successfully");
        }

        // response gagal menghapus photo
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {

        // validasi data page dan size
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Photo> photos = photoRepository.findByAlbumId(albumId, pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());

        // ambil semua data photo
        for (Photo photo : photos.getContent()) {
            PhotoResponse photoResponse = PhotoResponse.builder()
                    .id(photo.getId())
                    .title(photo.getTitle())
                    .url(photo.getUrl())
                    .thumbnailUrl(photo.getThumbnailUrl())
                    .albumId(photo.getAlbum().getId())
                    .build();

            // add photoResponse
            photoResponses.add(photoResponse);
        }

        // response PagedRespone
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
