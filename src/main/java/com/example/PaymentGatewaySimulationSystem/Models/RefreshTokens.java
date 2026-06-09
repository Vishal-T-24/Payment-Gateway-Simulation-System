package com.example.PaymentGatewaySimulationSystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokens {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private Users user;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public RefreshTokens( String token, Users user, LocalDateTime expiryDate, boolean isActive, LocalDateTime createdAt) {

        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
}
