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
@Table(name = "ledger")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ledger implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @OneToOne
        @JoinColumn(name = "payment_id", referencedColumnName = "id")
        private Payments payment;

        @ManyToOne
        @JoinColumn(name = "merchant_id", referencedColumnName = "id")
        private Merchants merchant;


        @Column(name = "credit")
        private BigDecimal credit;



        @Column(name = "status")
        private String status; // SUCCESS or FAILED


        @Column(name = "created_at")
        private LocalDateTime createdAt;

        public Ledger(Payments payment, Merchants merchant, BigDecimal credit, String status,LocalDateTime createdAt) {
            this.payment = payment;
            this.merchant = merchant;

            this.credit = credit;

            this.status = status;
            this.createdAt=createdAt;
        }


}
