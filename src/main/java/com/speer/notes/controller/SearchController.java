package com.speer.notes.controller;


import com.speer.notes.dto.response.NoteResponse;
import com.speer.notes.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Notes API Search", description = "Endpoints for notes searching")
@SecurityRequirement(name = "bearerAuth")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    @Operation(summary = "Search note by keywords",
            description = "Search note with keywords for the current user ")
    public ResponseEntity<Page<NoteResponse>> searchNotes(
            @Parameter(description = "Search text") @RequestParam("q") String query,
            Pageable pageable) {
        return ResponseEntity.ok(new PageImpl<>(searchService.searchNotes(query, pageable), pageable,  searchService.searchNotes(query, pageable).size()));
    }
}
