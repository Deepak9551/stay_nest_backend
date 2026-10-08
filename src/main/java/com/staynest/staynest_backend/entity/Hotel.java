package com.staynest.staynest_backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false , columnDefinition = "TEXT[]")
    private String[] photos; // Array of photo URLs for the hotel
    
    @Column(nullable = false , columnDefinition = "TEXT[]")
    private String[] amenities; // Array of amenities available in the hotel

     @CreationTimestamp
    @Column(updatable = false , name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column( name="updated_at")
    private LocalDateTime updatedAt;
    
    @Embedded
    private HotalContactInfo contactInfo; // Embedded contact information for the hotel

    private Boolean  active; // Indicates whether the hotel is active or not

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Room> room; // Reference to the associated room (if applicable)

    @ManyToOne(optional = false) // one owner can have many hotels
    private User owner;

}
