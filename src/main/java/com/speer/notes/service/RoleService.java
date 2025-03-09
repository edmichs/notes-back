package com.speer.notes.service;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.RoleRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.dto.response.RoleResponse;
import com.speer.notes.entity.ERole;
import com.speer.notes.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(ERole name);
    List<RoleResponse> getAllRoles();
    MessageResponse createRole(RoleRequest roleRequest);
    MessageResponse updateRole(Long id, RoleRequest roleRequest);
    MessageResponse deleteRole(Long id);
}
