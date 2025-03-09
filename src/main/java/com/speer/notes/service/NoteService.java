package com.speer.notes.service;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.ShareNoteRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface NoteService {
    List<NoteResponse> getAllNotes(Pageable pageable);
    NoteResponse getNoteById(Long id);
    NoteResponse createNote(NoteRequest noteRequest);
    NoteResponse updateNote(Long id, NoteRequest noteRequest);
    MessageResponse deleteNote(Long id);
    MessageResponse shareNote(Long id, ShareNoteRequest shareRequest);
}
