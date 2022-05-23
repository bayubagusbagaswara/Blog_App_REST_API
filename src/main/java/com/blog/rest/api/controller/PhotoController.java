package com.blog.rest.api.controller;

import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PhotoService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PhotoResponse> addPhoto(
            @Valid @RequestBody PhotoRequest photoRequest,
            @CurrentUser UserPrincipal currentUser) {

        PhotoResponse photoResponse = photoService.addPhoto(photoRequest, currentUser);
        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhotoById(
            @PathVariable(name = "id") Long id) {

        PhotoResponse photoResponse = photoService.getPhotoById(id);
        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<PhotoResponse>> getAllPhotos(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        final PagedResponse<PhotoResponse> allPhotos = photoService.getAllPhotos(page, size);
        return new ResponseEntity<>(allPhotos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PhotoResponse> updatePhoto(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody PhotoRequest photoRequest,
            @CurrentUser UserPrincipal currentUser) {

        PhotoResponse photoResponse = photoService.updatePhoto(id, photoRequest, currentUser);
        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePhoto(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser) {

        ApiResponse apiResponse = photoService.deletePhoto(id, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
