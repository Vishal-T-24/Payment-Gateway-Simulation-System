package com.example.PaymentGatewaySimulationSystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "api_logs")
@AllArgsConstructor
@NoArgsConstructor
public class Api_Logs {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id",referencedColumnName = "id",nullable = false)
    private Merchants merchant;

    @Column(name = "api_endpoint")
    private String endpoint;

    private String method;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Api_Logs( Merchants merchant, String endpoint, String method, String status, LocalDateTime createdAt) {

        this.merchant = merchant;
        this.endpoint = endpoint;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
    }
}
