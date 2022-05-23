package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.exception.ResponseEntityErrorException;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.album.AlbumRequest;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.AlbumService;
import com.blog.rest.api.service.PhotoService;
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
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Album> addAlbum(
            @Valid @RequestBody AlbumRequest albumRequest,
            @CurrentUser UserPrincipal currentUser) {

        final Album album = albumService.addAlbum(albumRequest, currentUser);
        return new ResponseEntity<>(album, HttpStatus.CREATED);
    }
}
