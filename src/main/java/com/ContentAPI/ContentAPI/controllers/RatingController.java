package com.ContentAPI.ContentAPI.controllers;

import com.ContentAPI.ContentAPI.domain.Rating;
import com.ContentAPI.ContentAPI.dtos.RatingRequestDTO;
import com.ContentAPI.ContentAPI.dtos.RatingResponseDTO;
import com.ContentAPI.ContentAPI.services.ContentService;
import com.ContentAPI.ContentAPI.services.RatingService;
import com.ContentAPI.ContentAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Rating Management", description = "Endpoints for managing ratings")
@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a rating", description = "Endpoint to create a new rating for content",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rating created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/{contentId}")
    public ResponseEntity<RatingResponseDTO> createRating(@PathVariable Long contentId,
                                                          @Valid @RequestBody RatingRequestDTO data) {
        Long userId = userService.getUserFromContext().getId();
        String userLogin = userService.getUserFromContext().getLogin();
        String contentTitle = contentService.findById(contentId).getTitle();
        Rating newRating = ratingService.createRating(userId, contentId, data);

        RatingResponseDTO ratingResponseDTO =
                new RatingResponseDTO(newRating.getScore(),
                        newRating.getComment(),
                        newRating.getCreatedAt(),
                        userLogin,
                        contentTitle);

        return ResponseEntity.status(HttpStatus.CREATED).body(ratingResponseDTO);
    }

    @Operation(summary = "Get ratings by content", description = "Endpoint to retrieve ratings for a specific content",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ratings retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Content not found",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/{contentId}")
    public ResponseEntity<List<RatingResponseDTO>> getRatingsByContent(@PathVariable Long contentId) {
        List<Rating> ratings = ratingService.getRatingsByContent(contentId);

        List<RatingResponseDTO> ratingDTO = ratings.stream()
                .map(rating -> new RatingResponseDTO(
                        rating.getScore(),
                        rating.getComment(),
                        rating.getCreatedAt(),
                        rating.getUser().getLogin(),
                        rating.getContent().getTitle()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ratingDTO);
    }

    @Operation(summary = "Get average rating by content", description = "Endpoint to retrieve the average rating for a specific content",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Average rating retrieved successfully",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Content not found",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/{contentId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long contentId) {
        Double averageRating = ratingService.getAverageRating(contentId);
        return ResponseEntity.status(HttpStatus.OK).body(averageRating);
    }

    @Operation(summary = "Update rating", description = "Endpoint to update an existing rating",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rating updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rating.class))),
                    @ApiResponse(responseCode = "404", description = "Rating not found",
                            content = @Content(mediaType = "application/json"))
            })
    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable Long id, @Valid @RequestBody Rating rating) {
        Rating updatedRating = ratingService.updateRating(id, rating);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRating);
    }

    @Operation(summary = "Delete rating", description = "Endpoint to delete an existing rating",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rating deleted successfully",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Rating not found",
                            content = @Content(mediaType = "application/json"))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
