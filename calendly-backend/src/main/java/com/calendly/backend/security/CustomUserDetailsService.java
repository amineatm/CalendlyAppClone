package com.calendly.backend.security;

import com.calendly.backend.repository.OrganizerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final OrganizerRepository organizerRepository;

    public CustomUserDetailsService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) organizerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Organizer not found with email: " + email));
    }
}
