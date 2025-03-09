package com.speer.notes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private String ownerUsername;
    private Set<String> sharedWith;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
