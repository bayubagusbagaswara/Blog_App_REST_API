package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.exception.ResponseEntityErrorException;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.payload.album.AlbumRequest;
import com.blog.rest.api.payload.album.AlbumResponse;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.AlbumService;
import com.blog.rest.api.service.PhotoService;
import com.blog.rest.api.utils.AppConstants;
import com.blog.rest.api.utils.AppUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    public AlbumController(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    // handler dan response jika terjadi error
    @ExceptionHandler(ResponseEntityErrorException.class)
    public ResponseEntity<ApiResponse> handleExceptions(ResponseEntityErrorException exception) {
        return exception.getApiResponse();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Album> addAlbum(
            @Valid @RequestBody AlbumRequest albumRequest,
            @CurrentUser UserPrincipal currentUser) {

        final Album album = albumService.addAlbum(albumRequest, currentUser);
        return new ResponseEntity<>(album, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable(name = "id") Long id) {
        final Album album = albumService.getAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping
    public PagedResponse<AlbumResponse> getAllAlbums(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        AppUtils.validatePageNumberAndSize(page, size);

        return albumService.getAllAlbums(page, size);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody AlbumRequest newAlbum,
            @CurrentUser UserPrincipal currentUser) {

        final AlbumResponse album = albumService.updateAlbum(id, newAlbum, currentUser);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAlbum(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser) {

        final ApiResponse apiResponse = albumService.deleteAlbum(id, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<PagedResponse<PhotoResponse>> getAllPhotosByAlbum(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<PhotoResponse> response = photoService.getAllPhotosByAlbum(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
