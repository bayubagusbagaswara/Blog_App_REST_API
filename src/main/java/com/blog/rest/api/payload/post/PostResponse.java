package com.blog.rest.api.payload.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String body;
    private String category;
    private List<String> tags;

    public PostResponse(String title, String body, String category) {
        this.title = title;
        this.body = body;
        this.category = category;
    }

    public PostResponse(String title, String body, String category, List<String> tags) {
        this.title = title;
        this.body = body;
        this.category = category;
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags == null ? null : new ArrayList<>(tags);
    }

    public void setTags(List<String> tags) {
        if (tags == null) {
            this.tags = null;
        } else {
            this.tags = Collections.unmodifiableList(tags);
        }
    }

}
