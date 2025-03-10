package com.speer.notes.units;

import com.speer.notes.controller.NoteController;
import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.ShareNoteRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import com.speer.notes.exception.ResourceNotFoundException;
import com.speer.notes.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User user;
    private Note note;
    private NoteRequest noteRequest;
    private Pageable pageable;
    private NoteResponse noteResponse;
    private Set<String> sharedWith;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        user = new User("test1", "test1@gmail.com", "test123");

        note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("Test Content");
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setOwner(user);

        noteRequest = new NoteRequest();
        noteRequest.setTitle("Test Note");
        noteRequest.setContent("Test Content");

        noteResponse = new NoteResponse();
        noteResponse.setId(1L);
        noteResponse.setCreatedAt(LocalDateTime.now());
        noteResponse.setUpdatedAt(LocalDateTime.now());
        noteResponse.setTitle("Test Note");
        noteResponse.setContent("Test Content");
        noteResponse.setOwnerUsername(user.getUsername());
        noteResponse.setSharedWith(sharedWith);


        // Set up security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should return all notes for authenticated user")
    void getAllNotes_ShouldReturnAllNotesForAuthenticatedUser() {
        List<NoteResponse> notes = Arrays.asList(noteResponse);

        when(noteService.getAllNotes(pageable)).thenReturn(notes);

        ResponseEntity<Page<NoteResponse>> response = noteController.getAllUserNotes(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getSize());
        assertEquals("Test Note", response.getBody().getContent().get(0).getTitle());
        assertEquals("Test Content", response.getBody().getContent().get(0).getContent());
        verify(noteService, times(1)).getAllNotes(pageable);
    }

    @Test
    @DisplayName("Should return note by ID when authorized")
    void getNoteById_ShouldReturnNoteWhenAuthorized() {
        // Arrange
        when(noteService.getNoteById(anyLong())).thenReturn(noteResponse);

        // Act
        ResponseEntity<NoteResponse> response = noteController.getNoteById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Note", response.getBody().getTitle());
        assertEquals("Test Content", response.getBody().getContent());
        verify(noteService, times(1)).getNoteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when note not found")
    void getNoteById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(noteService.getNoteById(anyLong())).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> noteController.getNoteById(1L));
        verify(noteService, times(1)).getNoteById(1L);
    }

    @Test
    @DisplayName("Should create new note successfully")
    void createNote_ShouldCreateNewNote() {
        // Arrange
        when(noteService.createNote(any(NoteRequest.class))).thenReturn(noteResponse);

        // Act
        ResponseEntity<NoteResponse> response = noteController.createNote(noteRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Note", response.getBody().getTitle());
        assertEquals("Test Content", response.getBody().getContent());
        verify(noteService, times(1)).createNote(noteRequest);
    }

    @Test
    @DisplayName("Should update note successfully")
    void updateNote_ShouldUpdateNote() {
        // Arrange
        when(noteService.updateNote(anyLong(), any(NoteRequest.class))).thenReturn(noteResponse);

        // Act
        ResponseEntity<NoteResponse> response = noteController.updateNote(1L, noteRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Note", response.getBody().getTitle());
        assertEquals("Test Content", response.getBody().getContent());
        verify(noteService, times(1)).updateNote(1L, noteRequest);
    }

    @Test
    @DisplayName("Should delete note successfully")
    void deleteNote_ShouldDeleteNote() {
        // Arrange
        doNothing().when(noteService).deleteNote(anyLong());

        // Act
        ResponseEntity<Void> response = noteController.deleteNote(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(noteService, times(1)).deleteNote(1L);
    }

    @Test
    @DisplayName("Should share note successfully")
    void shareNote_ShouldShareNote() {
        // Arrange
        ShareNoteRequest shareNoteRequest = new ShareNoteRequest();
        shareNoteRequest.setUsername("test2");
        doNothing().when(noteService).shareNote(anyLong(),any(ShareNoteRequest.class));

        // Act
        ResponseEntity<MessageResponse> response = noteController.shareNote(1L, shareNoteRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(noteService, times(1)).shareNote(1L, shareNoteRequest);
    }


}
