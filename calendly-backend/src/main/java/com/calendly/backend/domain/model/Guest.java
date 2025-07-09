package com.calendly.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Guest {

    @Id
    private String guestId;

    private String name;
    private String email;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
