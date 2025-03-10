package com.speer.notes.dto.response;

import com.speer.notes.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public RoleResponse(Role role) {
    }
}
