package com.blog.rest.api.payload.response;

import com.blog.rest.api.entity.Photo;
import com.blog.rest.api.entity.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableAsync
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlbumResponse {

    private Long id;

    private String title;

    private User user;

    private List<Photo> photo;

    public List<Photo> getPhoto() {
        return photo == null ? null : new ArrayList<>(photo);
    }

    public void setPhoto(List<Photo> photo) {
        if (photo == null) {
            this.photo = null;
        } else {
            this.photo = Collections.unmodifiableList(photo);
        }
    }

}
