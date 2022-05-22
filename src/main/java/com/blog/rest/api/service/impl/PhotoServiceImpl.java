package com.blog.rest.api.service.impl;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.entity.Photo;
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
import org.springframework.stereotype.Service;

import static com.blog.rest.api.utils.AppConstants.ALBUM;
import static com.blog.rest.api.utils.AppConstants.ID;

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
