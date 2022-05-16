package com.blog.rest.api.entity;

import com.blog.rest.api.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
// artinya saat data category ini diload atau diambil dari entity lain, maka hanya data id category saja yang diambil
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category extends UserDateAudit {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // relasi dengan Post, ini relasi bidirectional,
    // artinya dari sisi Category kita bisa mengambil data Post
    // 1 Category bisa memiliki banyak Post
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    public Category(String name) {
        super();
        this.name = name;
    }

    // method untuk mengambil data Post dari category
    public List<Post> getPosts() {
        return this.posts == null ? null : new ArrayList<>(this.posts);
    }

    // method untuk set Post
    // kita tidak bisa set Post dari sisi Category, makanya kita jadikan sebagai immutable
    // karena post hanya bisa diubah dari sisi Post
    public void setPosts(List<Post> posts) {
        if (posts == null) {
            this.posts = null;
        } else {
            this.posts = Collections.unmodifiableList(posts);
        }
    }
}
