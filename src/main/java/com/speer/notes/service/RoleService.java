package com.speer.notes.service;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.RoleRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.dto.response.RoleResponse;
import com.speer.notes.entity.ERole;
import com.speer.notes.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> getRoleByName(String name);
    Page<Role> getAllRoles(Pageable pageable);

    Page<Role> getAllRoles(int page, int size);

    Role getRoleById(Long id);
    MessageResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(Long id, RoleRequest roleRequest);
    MessageResponse deleteRole(Long id);
}
