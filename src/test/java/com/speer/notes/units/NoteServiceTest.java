package com.speer.notes.units;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.ShareNoteRequest;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import com.speer.notes.exception.ResourceNotFoundException;
import com.speer.notes.exception.UnauthorizedException;
import com.speer.notes.repository.NoteRepository;
import com.speer.notes.repository.UserRepository;
import com.speer.notes.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteService noteService;

    private User owner;
    private User sharedUser;
    private Note note;
    private NoteRequest noteRequest;
    private ShareNoteRequest shareNoteRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("owner");
        owner.setEmail("owner@example.com");

        sharedUser = new User();
        sharedUser.setId(2L);
        sharedUser.setUsername("shared");
        sharedUser.setEmail("shared@example.com");

        note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("Test Content");
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setOwner(owner);

        noteRequest = new NoteRequest();
        noteRequest.setTitle("Test Note");
        noteRequest.setContent("Test Content");

        shareNoteRequest = new ShareNoteRequest();
        shareNoteRequest.setUsername("shared");
    }

    @Test
    @DisplayName("Should return all notes for a user")
    void getAllNotesForUser_ShouldReturnAllNotes() {
        // Arrange
        List<Note> notes = Arrays.asList(note);
        when(noteRepository.findAllAccessibleByUser(owner)).thenReturn(notes);

        // Act
        List<NoteResponse> result = noteService.getAllNotes(pageable);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Note", result.get(0).getTitle());
        assertEquals("Test Content", result.get(0).getContent());
        verify(noteRepository, times(1)).findAllAccessibleByUser(owner);
    }

    @Test
    @DisplayName("Should return note by ID for authorized user")
    void getNoteByIdForUser_ShouldReturnNoteWhenAuthorized() {
        // Arrange
        when(noteRepository.findByIdAndAccessible(anyLong(), owner))
                .thenReturn(Optional.of(note));

        // Act
        NoteResponse result = noteService.getNoteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("owner", result.getOwnerUsername());
        verify(noteRepository, times(1)).findByIdAndAccessible(1L, owner);
    }

    @Test
    @DisplayName("Should create note successfully")
    void createNote_ShouldCreateNote() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        // Act
        NoteResponse result = noteService.createNote(noteRequest);

        // Assert
        assertEquals("Test Note", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("owner", result.getOwnerUsername());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during create")
    void createNote_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> noteService.createNote(noteRequest));
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("Should update note successfully")
    void updateNote_ShouldUpdateNote() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        // Act
        NoteResponse result = noteService.updateNote(1L, noteRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("owner", result.getOwnerUsername());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when note not found during update")
    void updateNote_ShouldThrowExceptionWhenNoteNotFound() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> noteService.updateNote(1L, noteRequest));
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when user not authorized during update")
    void updateNote_ShouldThrowExceptionWhenNotAuthorized() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> noteService.updateNote(1L, noteRequest));
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("Should delete note successfully")
    void deleteNote_ShouldDeleteNote() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));
        doNothing().when(noteRepository).delete(any(Note.class));

        // Act
        noteService.deleteNote(1L);

        // Assert
        verify(noteRepository, times(1)).delete(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when note not found during delete")
    void deleteNote_ShouldThrowExceptionWhenNoteNotFound() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> noteService.deleteNote(1L));
        verify(noteRepository, never()).delete(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when user not authorized during delete")
    void deleteNote_ShouldThrowExceptionWhenNotAuthorized() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> noteService.deleteNote(1L));
        verify(noteRepository, never()).delete(any(Note.class));
    }

    @Test
    @DisplayName("Should share note successfully")
    void shareNote_ShouldShareNote() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(sharedUser));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        // Act
        noteService.shareNote(1L, shareNoteRequest);

        // Assert
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when note not found during share")
    void shareNote_ShouldThrowExceptionWhenNoteNotFound() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> noteService.shareNote(1L, shareNoteRequest));
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when user not authorized during share")
    void shareNote_ShouldThrowExceptionWhenNotAuthorized() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> noteService.shareNote(1L, shareNoteRequest));
        verify(userRepository, never()).findByUsername(anyString());
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    @DisplayName("Should throw exception when shared user not found")
    void shareNote_ShouldThrowExceptionWhenSharedUserNotFound() {
        // Arrange
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> noteService.shareNote(1L, shareNoteRequest));
        verify(noteRepository, never()).save(any(Note.class));
    }

}
