package com.speer.notes.service.impl;

import com.speer.notes.dto.request.RoleRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.RoleResponse;
import com.speer.notes.entity.Role;
import com.speer.notes.exception.ResourceNotFoundException;
import com.speer.notes.repository.RoleRepository;
import com.speer.notes.service.RoleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;
    /**
     * @param name
     * @return
     */
    @Override
    public Optional<Role> getRoleByName(String name) {
        return  Optional.ofNullable(roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name)));
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Role> getAllRoles(Pageable pageable) {
        return  roleRepository.findAll(pageable);
    }


    /**
     * @return
     */
    @Override
    public Page<Role> getAllRoles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return  roleRepository.findAll(pageable);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Role getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        logger.info(role + " role found");
        return role;
    }

    /**
     * @param roleRequest
     * @return
     */
    @Override
    public MessageResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            return new MessageResponse("Error: Role is already taken!");
        }
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());

        roleRepository.save(role);
        return new MessageResponse("Role created!");
    }

    /**
     * @param id
     * @param roleRequest
     * @return
     */
    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        role.setName(roleRequest.getName());
        roleRepository.save(role);
        return new RoleResponse(role);
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
