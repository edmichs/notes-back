package com.speer.notes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    private String content;
}
