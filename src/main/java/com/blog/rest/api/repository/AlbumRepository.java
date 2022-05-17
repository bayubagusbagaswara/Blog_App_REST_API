package com.blog.rest.api.repository;

import com.blog.rest.api.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // mencari album berdasarkan created by dari user tertentu
    Page<Album> findByCreatedBy(Long userId, Pageable pageable);
}
