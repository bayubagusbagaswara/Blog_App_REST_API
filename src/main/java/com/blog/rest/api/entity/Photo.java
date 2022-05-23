package com.blog.rest.api.entity;

import com.blog.rest.api.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "photos", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "url", nullable = false)
    private String url;

    @NotBlank
    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", foreignKey = @ForeignKey(name = "fk_photo_album_id"), referencedColumnName = "id")
    private Album album;

    public Photo(@NotBlank String title, @NotBlank String url, @NotBlank String thumbnailUrl, Album album) {
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.album = album;
    }

    @JsonIgnore
    public Album getAlbum() {
        return album;
    }
}
