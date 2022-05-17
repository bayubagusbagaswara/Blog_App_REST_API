package com.blog.rest.api.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

// class UserPrincipal ini implement interface UserDetail
// yang artinya class ini bisa mengakses data detail dari User
@Data
public class UserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    // constructor
    public UserPrincipal(Long id, String firstName, String lastName, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;

        // cek apakah user memiliki authority atau hak ijin akses
        if (authorities == null) {
            // user belum memiliki hak ijin akses
            this.authorities = null;
        } else {
            // user sudah memiliki hak ijin akses
            this.authorities = new ArrayList<>(authorities);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // cek apakah authorities bernilai null, jika true maka bernilai null
        // jika false artinya ada authorities nya
        return authorities == null ? null : new ArrayList<>(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) object;
        return Objects.equals(id, that.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
