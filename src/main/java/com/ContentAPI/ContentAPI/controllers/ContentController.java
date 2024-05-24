package com.ContentAPI.ContentAPI.controllers;

import com.ContentAPI.ContentAPI.domain.Content;
import com.ContentAPI.ContentAPI.dtos.ContentRequestDTO;
import com.ContentAPI.ContentAPI.dtos.ContentResponseDTO;
import com.ContentAPI.ContentAPI.exceptions.InvalidDataException;
import com.ContentAPI.ContentAPI.exceptions.ResourceNotFoundException;
import com.ContentAPI.ContentAPI.services.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Content Management", description = "Endpoints for managing content")
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Operation(summary = "Create new content", description = "Endpoint to create new content",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Content created successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @Schema(implementation = Content.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
            })
    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody @Valid ContentRequestDTO content) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(contentService.saveContent(content));
        } catch (InvalidDataException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }
    @Operation(summary = "Get all content", description = "Endpoint to retrieve all content",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Content retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @Schema(implementation = Content.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
            })
    @GetMapping
    public ResponseEntity<List<ContentResponseDTO>> getAllContent() {
        List<ContentResponseDTO> contentResponseDTOList = contentService.findAll().stream()
                .map(content -> new ContentResponseDTO(
                        content.getTitle(),
                        content.getDescription(),
                        content.getCategory(),
                        content.getThumbnailUrl(),
                        content.getContentUrl(),
                        content.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(contentResponseDTOList);
    }

    @Operation(summary = "Get content by ID", description = "Endpoint to retrieve content by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Content retrieved successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @Schema(implementation = Content.class))),
                    @ApiResponse(responseCode = "404", description = "Content not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
            })
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(contentService.findById(id));
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @Operation(summary = "Update content by ID", description = "Endpoint to update content by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Content updated successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @Schema(implementation = Content.class))),
                    @ApiResponse(responseCode = "404", description = "Content not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
            })
    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable Long id, @RequestBody @Valid ContentRequestDTO content) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(contentService.updateContent(id, content));
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
    @Operation(summary = "Delete content by ID", description = "Endpoint to delete content by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Content deleted successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Content not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
            })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        try {
            contentService.deleteContent(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
