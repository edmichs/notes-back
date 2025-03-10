package com.speer.notes.repository;

import com.speer.notes.entity.Note;
import com.speer.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwner(User owner);

    @Query("SELECT n FROM Note n WHERE n.owner = :user OR :user MEMBER OF n.sharedWith")
    List<Note> findAllAccessibleByUser(@Param("user") User user);

    @Query("SELECT n FROM Note n WHERE n.owner = :user AND n.id = :noteId")
    Optional<Note> findByIdAndOwner(@Param("noteId") Long noteId, @Param("user") User user);

    @Query("SELECT n FROM Note n WHERE (n.owner = :user OR :user MEMBER OF n.sharedWith) AND n.id = :noteId")
    Optional<Note> findByIdAndAccessible(@Param("noteId") Long noteId, @Param("user") User user);

    @Query(value = "SELECT * FROM notes n WHERE (n.owner_id = :userId OR EXISTS (SELECT 1 FROM note_shares ns WHERE ns.note_id = n.id AND ns.user_id = :userId)) AND (to_tsvector('english', n.title || ' ' || coalesce(n.content, '')) @@ to_tsquery('english', :searchQuery))", nativeQuery = true)
    List<Note> searchNotes(@Param("searchQuery") String searchQuery, @Param("userId") Long userId);

    List<Note> owner(User owner);
}
