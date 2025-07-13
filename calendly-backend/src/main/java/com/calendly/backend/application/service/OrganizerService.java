package com.calendly.backend.application.service;

import com.calendly.backend.domain.model.Organizer;
import com.calendly.backend.domain.repository.IOrganizerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizerService {

    private final IOrganizerRepository organizerRepository;

    public OrganizerService(IOrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    public Organizer create(Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    public Optional<Organizer> getById(String id) {
        return organizerRepository.findById(id);
    }

    public List<Organizer> getAll() {
        return organizerRepository.findAll();
    }

    public Organizer update(String id, Organizer updated) {
        updated.setOrganizerId(id);
        return organizerRepository.save(updated);
    }

    public void delete(String id) {
        organizerRepository.deleteById(id);
    }
}
