package com.calendly.backend.domain.repository;

import com.calendly.backend.domain.model.Booking;
import java.util.List;

public interface IBookingRepository extends IRepository<Booking, String> {
    List<Booking> findByEventId(String eventId);
    List<Booking> findByGuestId(String guestId);
}
