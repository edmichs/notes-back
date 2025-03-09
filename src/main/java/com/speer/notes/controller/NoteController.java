package com.speer.notes.controller;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.ShareNoteRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.User;
import com.speer.notes.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "Notes API", description = "Endpoints for notes managements")
@SecurityRequirement(name = "bearerAuth")
public class NoteController {

    @Autowired
    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Retrieve all notes of user ",
            description = "Return all notes of authenticate user")
    public ResponseEntity<Page<NoteResponse>> getAllUserNotes(
            Pageable pageable) {
        return ResponseEntity.ok((Page<NoteResponse>) noteService.getAllNotes(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one note by ID",
            description = "Return specific not for principal user")
    public ResponseEntity<NoteResponse> getNoteById(
            @Parameter(description = "ID de la note") @PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping
    @Operation(summary = "Create new note",
            description = "Create new note for principal user ")
    @ApiResponse(responseCode = "201", description = "Note create successful")
    public ResponseEntity<NoteResponse> createNote(
            @Valid @RequestBody NoteRequest noteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteService.createNote(noteRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note",
            description = "Delete note for principql user ")
    @ApiResponse(responseCode = "204", description = "Note deleted successful")
    public ResponseEntity<Void> deleteNote(
            @Parameter(description = "noteID") @PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing note",
            description = "Update existing note for principal user")
    public ResponseEntity<NoteResponse> updateNote(
            @Parameter(description = "noteID") @PathVariable Long id,
            @Valid @RequestBody NoteRequest noteRequest) {
        return ResponseEntity.ok(noteService.updateNote(id, noteRequest));
    }

    @PostMapping("/{id}/share")
    @Operation(summary = "Share note with another user ",
            description = "Share principal user note's to another user")
    public ResponseEntity<MessageResponse> shareNote(
            @Parameter(description = "noteID") @PathVariable Long id,
            @Valid @RequestBody ShareNoteRequest shareRequest) {
        return ResponseEntity.ok(noteService.shareNote(id, shareRequest));
    }
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        return ResponseEntity.ok(currentUser);

    }
}
