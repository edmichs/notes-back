package com.speer.notes.repository;

import com.speer.notes.entity.ERole;
import com.speer.notes.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Page<Role> findAll(Pageable pageable);
    Boolean existsByName(String name);
}
