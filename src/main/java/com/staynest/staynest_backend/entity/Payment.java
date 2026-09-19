package com.staynest.staynest_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.staynest.staynest_backend.entity.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Payment {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique=true)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

       @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

       @OneToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "booking")
    private Booking booking;

            @CreationTimestamp
    @Column(updatable = false , name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column( name="updated_at")
    private LocalDateTime updatedAt;
}
