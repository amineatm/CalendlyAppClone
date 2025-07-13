-- V1: This script creates the initial schema for the Calendly clone application.

-- Table for Organizers who create events
CREATE TABLE organizers (
                            organizer_id VARCHAR(36) NOT NULL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            role VARCHAR(255),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table for Guests who book events
CREATE TABLE guests (
                        guest_id VARCHAR(36) NOT NULL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table for Events created by Organizers
CREATE TABLE events (
                        event_id VARCHAR(36) NOT NULL PRIMARY KEY,
                        organizer_id VARCHAR(36) NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        start_time DATETIME,
                        end_time DATETIME,
                        location VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (organizer_id) REFERENCES organizers(organizer_id) ON DELETE CASCADE,
                        INDEX idx_organizer_id (organizer_id)
);

-- Table for defining available time slots for an Event
CREATE TABLE availabilities (
                                availability_id VARCHAR(36) NOT NULL PRIMARY KEY,
                                event_id VARCHAR(36) NOT NULL,
                                date DATE NOT NULL,
                                start_time TIME NOT NULL,
                                end_time TIME NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
                                INDEX idx_event_id_avail (event_id)
);

-- Table for Bookings made by Guests for an Event
CREATE TABLE bookings (
                          booking_id VARCHAR(36) NOT NULL PRIMARY KEY,
                          event_id VARCHAR(36) NOT NULL,
                          guest_id VARCHAR(36) NOT NULL,
                          booked_at DATETIME NOT NULL,
                          status VARCHAR(50),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
                          FOREIGN KEY (guest_id) REFERENCES guests(guest_id) ON DELETE CASCADE,
                          INDEX idx_event_id_book (event_id),
                          INDEX idx_guest_id_book (guest_id)
);
