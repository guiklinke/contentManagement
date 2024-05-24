package com.ContentAPI.ContentAPI.services;

import com.ContentAPI.ContentAPI.config.SecurityConfiguration;
import com.ContentAPI.ContentAPI.domain.Role;
import com.ContentAPI.ContentAPI.domain.User;
import com.ContentAPI.ContentAPI.dtos.LoginRequestDTO;
import com.ContentAPI.ContentAPI.dtos.RegisterRequestDTO;
import com.ContentAPI.ContentAPI.exceptions.DuplicateUserException;
import com.ContentAPI.ContentAPI.exceptions.InvalidCredentialsException;
import com.ContentAPI.ContentAPI.exceptions.InvalidDataException;
import com.ContentAPI.ContentAPI.exceptions.UserNotFoundException;
import com.ContentAPI.ContentAPI.repositories.RoleRepository;
import com.ContentAPI.ContentAPI.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AuthenticationService{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenService tokenService;

    public String login(LoginRequestDTO loginRequestDTO) throws Exception {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDTO.login(), loginRequestDTO.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            return tokenService.generateToken((User) auth.getPrincipal());
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid username or password.");
        } catch (Exception e) {
            throw new LoginException("An error occurred during login.");
        }

    }

    @Transactional
        public User register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByLogin(registerRequestDTO.login())) {
            throw new DuplicateUserException("Error: User already exists");
        }

        if (registerRequestDTO.password().length() < 8) {
            throw new InvalidDataException("Error: Password must be at least 8 characters long");
        }


        String encodedPassword = securityConfiguration.passwordEncoder().encode(registerRequestDTO.password());

        User newUser = User.builder().login(registerRequestDTO.login())
                .password(encodedPassword)
                .createdAt(LocalDateTime.now())
                .roles(List.of(Role.builder().name(registerRequestDTO.role()).build()))
                .build();

        try {
            return userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Error: Invalid input data");
        } catch (Exception e) {
            throw new ServiceException("Error: Failed to register user");
        }

    }

}
