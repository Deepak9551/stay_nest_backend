package com.staynest.staynest_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@Table(name = "inventory"
    , uniqueConstraints = {@UniqueConstraint( name="uk_hotel_room_date",columnNames = {"hotel_id", "room_id", "date"})}
)
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // Reference to the associated room

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel; // Reference to the associated hotel

    @Column(nullable = false)
    private LocalDate  date; // Date for which the inventory is being tracked

    @Column(nullable = false, name="booked_count",columnDefinition = "INTEGER DEFAULT 0")
    @Builder.Default
    private Integer bookedCount = 0; // count of booked hotels

    @Column(nullable = false, name="reversed_count" ,columnDefinition = "INTEGER DEFAULT 0")
    @Builder.Default
    private Integer reversedCount=0;

    @Column(nullable = false, name="total_count")
    private Integer totalCount; // total number of room in a hotel

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal surgeFactor; // Surge factor for dynamic pricing (if applicable)

    private BigDecimal price; //  ( basePrice *  surgeFactor )

    private String city;

    private Boolean closed; // Indicates whether the inventory for this date is closed for booking

        @CreationTimestamp
    @Column(updatable = false , name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column( name="updated_at")
    private LocalDateTime updatedAt;



}
