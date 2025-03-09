package com.speer.notes.service.impl;

import com.speer.notes.config.security.UserDetailsImpl;
import com.speer.notes.dto.NoteMapper;
import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.request.ShareNoteRequest;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import com.speer.notes.exception.ResourceNotFoundException;
import com.speer.notes.exception.UnauthorizedException;
import com.speer.notes.repository.NoteRepository;
import com.speer.notes.repository.UserRepository;
import com.speer.notes.service.NoteService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteMapper noteMapper;
    /**
     * @return
     */
    @Override
    public List<NoteResponse> getAllNotes() {
        User currentUser = getCurrentUser();
        List<Note> notes = noteRepository.findAllAccessibleByUser(currentUser);
        return noteMapper.toNoteResponseList(notes);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public NoteResponse getNoteById(Long id) {
        User currentUser = getCurrentUser();
        Note note = noteRepository.findByIdAndAccessible(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        return noteMapper.toNoteResponse(note);
    }

    /**
     * @param noteRequest
     * @return
     */
    @Override
    public NoteResponse createNote(NoteRequest noteRequest) {
        User currentUser = getCurrentUser();

        Note note = noteMapper.toNote(noteRequest);
        note.setOwner(currentUser);
        Note savedNote = noteRepository.save(note);

        return noteMapper.toNoteResponse(savedNote);
    }

    /**
     * @param id
     * @param noteRequest
     * @return
     */
    @Override
    public NoteResponse updateNote(Long id, NoteRequest noteRequest) {
        User currentUser = getCurrentUser();

        Note note = noteRepository.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        noteMapper.updateNoteFromRequest(noteRequest, note);
        Note updatedNote = noteRepository.save(note);

        return noteMapper.toNoteResponse(updatedNote);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public MessageResponse deleteNote(Long id) {
        User currentUser = getCurrentUser();

        Note note = noteRepository.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        noteRepository.delete(note);

        return new MessageResponse("Note deleted successfully");
    }

    /**
     * @param id
     * @param shareRequest
     * @return
     */
    @Override
    public MessageResponse shareNote(Long id, ShareNoteRequest shareRequest) {
        User currentUser = getCurrentUser();

        Note note = noteRepository.findByIdAndOwner(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        User targetUser = userRepository.findByUsername(shareRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + shareRequest.getUsername()));

        if (targetUser.getId().equals(currentUser.getId())) {
            return new MessageResponse("You cannot share a note with yourself");
        }

        note.getSharedWith().add(targetUser);
        noteRepository.save(note);

        return new MessageResponse("Note shared successfully with " + targetUser.getUsername());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
