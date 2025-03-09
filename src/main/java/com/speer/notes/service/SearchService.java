package com.speer.notes.service;

import com.speer.notes.dto.response.NoteResponse;

import java.util.List;

public interface SearchService {
    List<NoteResponse> searchNotes(String query);
}
