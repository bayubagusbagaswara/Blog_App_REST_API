package com.blog.rest.api.controller;

import com.blog.rest.api.payload.photo.PhotoRequest;
import com.blog.rest.api.payload.photo.PhotoResponse;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
