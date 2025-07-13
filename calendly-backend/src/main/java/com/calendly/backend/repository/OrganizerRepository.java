package com.calendly.backend.repository;


import com.calendly.backend.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, String> {
    Optional<Object> findByEmail(String email);

}