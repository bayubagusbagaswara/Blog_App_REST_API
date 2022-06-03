package com.blog.rest.api.entity;

import com.blog.rest.api.entity.audit.UserDateAudit;
import com.blog.rest.api.entity.user.User;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    @Column(name = "body")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_comment_post_id"), referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_user_id"), referencedColumnName = "id")
    private User user;

    public Comment(@NotBlank @Size(min = 10, message = "Comment body must be minimum 10 characters") String body) {
        this.body = body;
    }

    @JsonIgnore
    public Post getPost() {
        return post;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }
}
