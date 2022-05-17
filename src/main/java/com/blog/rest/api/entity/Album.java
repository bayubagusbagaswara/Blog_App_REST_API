package com.blog.rest.api.entity;

import com.blog.rest.api.entity.audit.UserDateAudit;
import com.blog.rest.api.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "albums", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
@Data
public class Album extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_album_user_id"), referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public List<Photo> getPhoto() {
        return this.photos == null ? null : new ArrayList<>(this.photos);
    }

    public void setPhoto(List<Photo> photo) {
        if (photo == null) {
            this.photos = null;
        } else {
            this.photos = Collections.unmodifiableList(photo);
        }
    }

}
