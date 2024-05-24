package com.ContentAPI.ContentAPI.controllers;


import com.ContentAPI.ContentAPI.domain.User;
import com.ContentAPI.ContentAPI.dtos.LoginRequestDTO;
import com.ContentAPI.ContentAPI.dtos.RegisterRequestDTO;
import com.ContentAPI.ContentAPI.dtos.RegisterResponseDTO;
import com.ContentAPI.ContentAPI.exceptions.*;
import com.ContentAPI.ContentAPI.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Authentication", description = "Endpoint for proceeding with authentication in the API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Endpoint for user registration",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully registered",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "422", description = "Invalid input data",
                            content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> createUser(@Valid @RequestBody RegisterRequestDTO registerRequestDto) {

            User user = authenticationService.register(registerRequestDto);
            RegisterResponseDTO responseDTO = new RegisterResponseDTO(user.getLogin(), user.getCreatedAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    @Operation(summary = "Authenticate user", description = "Endpoint for user login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful, returns a bearer token",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequestDTO loginUserDto) {
        try {
            String token = authenticationService.login(loginUserDto);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (InvalidCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", ex);
        }
    }
}
