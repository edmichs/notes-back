package com.speer.notes.units;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.ShareNoteRequest;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import com.speer.notes.repository.NoteRepository;
import com.speer.notes.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private SearchService searchService;

    private Pageable pageable;
    private Note note;
    private User owner;

    @BeforeEach
    void setUp() {

        owner = new User();
        owner.setId(1L);
        owner.setUsername("owner");
        owner.setEmail("owner@example.com");

        note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("Test Content");
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setOwner(owner);

    }

    @Test
    @DisplayName("Should search notes successfully")
    void searchNotes_ShouldReturnMatchingNotes() {
        // Arrange
        List<Note> notePage = Arrays.asList(note);
        when(noteRepository.searchNotes(anyString(), anyLong())).thenReturn(notePage);

        // Act
        List<NoteResponse> result = searchService.searchNotes("test", pageable);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Note", result.get(0).getTitle());
        assertEquals("Test Content", result.get(0).getContent());
        verify(noteRepository, times(1)).searchNotes(anyString(), anyLong());
    }
}
