package com.blog.rest.api.entity.user;

import com.blog.rest.api.entity.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
public class Address extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "suite")
    private String suite;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private String zipcode;

    @OneToOne(mappedBy = "address")
    private User user;

    public Address(String street, String suite, String city, String zipcode) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    @JsonIgnore
    @Override
    public Instant getUpdatedAt() {
        return super.getUpdatedAt();
    }

    @JsonIgnore
    @Override
    public void setCreatedAt(Instant createdAt) {
        super.setCreatedAt(createdAt);
    }

    @JsonIgnore
    @Override
    public void setUpdatedAt(Instant updatedAt) {
        super.setUpdatedAt(updatedAt);
    }

    @JsonIgnore
    @Override
    public String getCreatedBy() {
        return super.getCreatedBy();
    }

    @JsonIgnore
    @Override
    public String getUpdatedBy() {
        return super.getUpdatedBy();
    }

    @JsonIgnore
    @Override
    public void setCreatedBy(String createdBy) {
        super.setCreatedBy(createdBy);
    }

    @JsonIgnore
    @Override
    public void setUpdatedBy(String updatedBy) {
        super.setUpdatedBy(updatedBy);
    }
}
