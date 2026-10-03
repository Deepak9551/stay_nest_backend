package com.staynest.staynest_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // one object present on the end => EAGER Fetching
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel; // Reference to the associated hotel

    private String type; // Type of the room (e.g., Single, Double, Suite)

    @Column(name = "base_price", nullable = false,precision = 10, scale = 2)
    private BigDecimal basePrice; // Base Price of the room

        @Column(nullable = false , columnDefinition = "TEXT[]")
    private String[] photos; // Array of photo URLs for the hotel
    
    @Column(nullable = false , columnDefinition = "TEXT[]")
    private String[] amenities; // Array of amenities available in the hotel

    @Column
    private Integer totalCount; // Total number of rooms of this type available in the hotel

    @Column(nullable = false)
    private Integer capacity; // Maximum number of guests the room can accommodate



    @CreationTimestamp
    @Column(updatable = false , name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column( name="updated_at")
    private LocalDateTime updatedAt;


}
