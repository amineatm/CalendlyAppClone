package com.calendly.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Organizer {

    @Id
    private String organizerId;

    private String name;
    private String email;
    private String role;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> events;
}