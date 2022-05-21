package com.blog.rest.api.repository;

import com.blog.rest.api.entity.Post;
import com.blog.rest.api.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // mencari Post berdasarkan created by user tertentu
    Page<Post> findByCreatedBy(Long userId, Pageable pageable);

    // mencari Post berdasarkan category
    Page<Post> findByCategory(Long categoryId, Pageable pageable);

    // mencari Post berdasarkan tags, tags bisa lebih dari satu
//    Page<Post> findByTags(List<Tag> tags, Pageable pageable);

    // menghitung jumlah post berdasarkan user yang membuatnya
    Long countByCreatedBy(Long userId);
}
