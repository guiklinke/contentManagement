package com.ContentAPI.ContentAPI.controllers;

import com.ContentAPI.ContentAPI.domain.User;
import com.ContentAPI.ContentAPI.exceptions.ResourceNotFoundException;
import com.ContentAPI.ContentAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "User Management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get current user", description = "Endpoint to get the currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved current user",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        try {
            User currentUser = userService.getUserFromContext();
            return ResponseEntity.status(HttpStatus.OK).body(currentUser);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

}
