package com.speer.notes.service.impl;

import com.speer.notes.config.security.UserDetailsImpl;
import com.speer.notes.dto.NoteMapper;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import com.speer.notes.exception.UnauthorizedException;
import com.speer.notes.repository.NoteRepository;
import com.speer.notes.repository.UserRepository;
import com.speer.notes.service.SearchService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteMapper noteMapper;

    /**
     * @param query
     * @return
     */
    @Override
    public List<NoteResponse> searchNotes(String query) {
        User currentUser = getCurrentUser();

        String formattedQuery = formatSearchQuery(query);

        List<Note> notes = noteRepository.searchNotes(formattedQuery, currentUser.getId());
        return noteMapper.toNoteResponseList(notes);
    }

    private String formatSearchQuery(String query) {
        String[] words = query.replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb.append(words[i]).append(":*");
            if (i < words.length - 1) {
                sb.append(" & ");
            }
        }

        return sb.toString();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
