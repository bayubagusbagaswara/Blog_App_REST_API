package com.blog.rest.api.repository;

import com.blog.rest.api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    // mencari Tag berdasarkan name
    Tag findByName(String name);
}
