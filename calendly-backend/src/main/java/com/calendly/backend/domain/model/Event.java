package com.calendly.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Event {

    @Id
    private String eventId;

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<com.calendly.backend.domain.model.Availability> availabilities;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
