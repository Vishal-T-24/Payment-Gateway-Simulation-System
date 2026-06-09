package com.example.PaymentGatewaySimulationSystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payments implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="merchant_id",referencedColumnName = "id",nullable = false)
    private Merchants merchant;

    private String order_id;

    @Column(precision = 19,scale = 2)
    private BigDecimal amount;

    private String currency;

    private String status;

    private String payment_method;

    @Column(name="idempotency_key")
    private String idempotencyKey;

    private int version;

    @Column(name="created_at")
   private LocalDateTime created_At;

    @Column(name="updated_at")
   private LocalDateTime upadted_At;

    public Payments( Merchants merchant, String order_id, BigDecimal amount, String currency, String status, String payment_method, String idempotencyKey, int version, LocalDateTime created_At, LocalDateTime upadted_At) {

        this.merchant = merchant;
        this.order_id = order_id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.payment_method = payment_method;
        this.idempotencyKey = idempotencyKey;
        this.version = version;
        this.created_At = created_At;
        this.upadted_At = upadted_At;
    }
}
