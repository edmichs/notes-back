package com.speer.notes.dto;

import com.speer.notes.dto.request.NoteRequest;
import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    @Mapping(source = "owner.username", target = "ownerUsername")
    @Mapping(source = "sharedWith", target = "sharedWith", qualifiedByName = "usersToUsernames")
    NoteResponse toNoteResponse(Note note);

    List<NoteResponse> toNoteResponseList(List<Note> notes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "sharedWith", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Note toNote(NoteRequest noteRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "sharedWith", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateNoteFromRequest(NoteRequest noteRequest, @MappingTarget Note note);

    @Named("usersToUsernames")
    default Set<String> usersToUsernames(Set<User> users) {
        if (users == null) {
            return Set.of();
        }
        return users.stream().map(User::getUsername).collect(Collectors.toSet());
    }
}
