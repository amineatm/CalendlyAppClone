package com.calendly.backend.domain.repository;

import com.calendly.backend.domain.model.Availability;

import java.util.List;

public interface IAvailabilityRepository extends IRepository<Availability, String> {
    List<Availability> findByEventId(String eventId);
}