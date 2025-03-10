package com.speer.notes.service;

import com.speer.notes.dto.response.NoteResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {
    List<NoteResponse> searchNotes(String query, Pageable pageable);
}
