package com.ContentAPI.ContentAPI.services;

import com.ContentAPI.ContentAPI.domain.User;
import com.ContentAPI.ContentAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
    public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
