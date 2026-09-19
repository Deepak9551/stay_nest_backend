package com.staynest.staynest_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.staynest.staynest_backend.entity.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "bookings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "room_id", nullable = false)
    private Room room; // Reference to the associated room

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel; // Reference to the associated hotel

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the associated user

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private Integer roomCount; // Number of rooms booked

    @Column(nullable = false)
    private LocalDate checkInDate; // Check-in date for the booking

    private LocalDate checkOutDate; // Check-out date for the booking

            @CreationTimestamp
    @Column(updatable = false , name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column( name="updated_at")
    private LocalDateTime updatedAt;

   
//    @JoinColumn(name = "payment_id")
//    @OneToOne(fetch = FetchType.LAZY)
//    private Payment payment;

    private BigDecimal amount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_guests",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    private List<Guest> guests; // List of guests associated with the booking
}
