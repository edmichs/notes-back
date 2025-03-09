package com.speer.notes.controller;


import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.RoleRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.dto.response.RoleResponse;
import com.speer.notes.entity.Role;
import com.speer.notes.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role User Management", description = "Authentication management API")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @Operation(summary = "Retrieve all roles ",
            description = "Return all roles")
    public ResponseEntity<Page<Role>> getAllRoles(
            Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by id",
            description = "Return role by id")
    public ResponseEntity<Role> getRoleById(
            @Parameter(description = "roleID") @PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role",
            description = "Update role")
    public ResponseEntity<RoleResponse> updateRole(
            @Parameter(description = "ID de la note") @PathVariable Long id,
            @Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.ok(roleService.updateRole(id, roleRequest));
    }

    @PostMapping
    @Operation(summary = "Create new role",
            description = "Create new role")
    @ApiResponse(responseCode = "201", description = "Role created successful")
    public ResponseEntity<MessageResponse> createRole(
            @Valid @RequestBody RoleRequest roleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.createRole(roleRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Role",
            description = "Delete role")
    @ApiResponse(responseCode = "204", description = "Role deleted successful")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "roleID") @PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
