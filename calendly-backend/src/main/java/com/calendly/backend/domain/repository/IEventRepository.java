package com.calendly.backend.domain.repository;

import com.calendly.backend.domain.model.Event;

import java.util.List;

public interface IEventRepository extends IRepository<Event, String> {
    List<Event> findByOrganizerId(String organizerId);
}