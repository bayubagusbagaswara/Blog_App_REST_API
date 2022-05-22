package com.blog.rest.api.entity;

import com.blog.rest.api.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class Tag extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = -5298707266367331514L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // buat table briging antara Tag dan Post, karena relasinya adalah Many To Many
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_post_tag_tag_id"), referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_post_tag_post_id"), referencedColumnName = "id"))
    private List<Post> posts;

    // Constructor with parameter is name tag
    public Tag(String name) {
        super();
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts == null ? null : new ArrayList<>(posts);
    }

    public void setPosts(List<Post> posts) {
        if (posts == null) {
            this.posts = null;
        } else {
            this.posts = Collections.unmodifiableList(posts);
        }
    }
}
