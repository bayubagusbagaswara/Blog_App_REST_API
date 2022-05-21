package com.blog.rest.api.service;

import com.blog.rest.api.entity.Album;
import com.blog.rest.api.payload.request.AlbumRequest;
import com.blog.rest.api.security.UserPrincipal;

public interface AlbumService {

    Album addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser);

}
