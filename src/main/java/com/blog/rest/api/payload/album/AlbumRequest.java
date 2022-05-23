package com.blog.rest.api.payload.album;

import com.blog.rest.api.entity.Photo;
import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.payload.UserDateAuditPayload;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequest extends UserDateAuditPayload {

    private Long id;

    private String title;

    private User user;

    private List<Photo> photos;

    // get photo
    public List<Photo> getPhoto() {
        return photos == null ? null : new ArrayList<>(photos);
    }

    // set photo
    public void setPhoto(List<Photo> photo) {
        if (photo == null) {
            this.photos = null;
        } else {
            this.photos = Collections.unmodifiableList(photo);
        }
    }

}
