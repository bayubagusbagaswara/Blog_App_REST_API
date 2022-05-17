package com.blog.rest.api.repository;

import com.blog.rest.api.entity.role.Role;
import com.blog.rest.api.entity.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // mencari role berdasarkan name, roleName diambil dari data enum
    Optional<Role> findByName(RoleName name);
}
