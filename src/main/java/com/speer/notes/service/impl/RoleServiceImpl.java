package com.speer.notes.service.impl;

import com.speer.notes.dto.request.RoleRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.RoleResponse;
import com.speer.notes.entity.ERole;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.Role;
import com.speer.notes.exception.ResourceNotFoundException;
import com.speer.notes.repository.RoleRepository;
import com.speer.notes.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    /**
     * @param name
     * @return
     */
    @Override
    public Optional<Role> findByName(ERole name) {
        return Optional.empty();
    }

    /**
     * @return
     */
    @Override
    public List<RoleResponse> getAllRoles() {
        return List.of();
    }

    /**
     * @param roleRequest
     * @return
     */
    @Override
    public MessageResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            return new MessageResponse("Error: Username is already taken!");
        }
        Role role = new Role(roleRequest.getName());
        roleRepository.save(role);
        return new MessageResponse("Role created!");
    }

    /**
     * @param id
     * @param roleRequest
     * @return
     */
    @Override
    public MessageResponse updateRole(Long id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        role.setName(roleRequest.getName());
        roleRepository.save(role);
        return new MessageResponse("Role updated!");
    }

    /**
     * @param id
     * @return
     */
    @Override
    public MessageResponse deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        roleRepository.delete(role);
        return new MessageResponse("Role deleted!");
    }
}
