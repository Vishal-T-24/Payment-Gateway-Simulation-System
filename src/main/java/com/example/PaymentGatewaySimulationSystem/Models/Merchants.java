package com.example.PaymentGatewaySimulationSystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="merchants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchants implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id", nullable = false,unique = true)
    private Users user;

    @Column(name="name")
    private String name;

    @Column(name = "publickey",unique = true)
    public String publicKey;

    @Column(name="secretkey",unique = true)
    public String secretKey;

    @Column(name="webhook_url")
    private String webhook_url;

    @Column(name="created_at")
    private LocalDateTime created_At;

    public Merchants(Users user, String name, String public_Key,String secret_Key, String webhook_url,LocalDateTime created_At){
        this.user=user;
        this.name=name;
        this.publicKey=public_Key;
        this.secretKey=secret_Key;
        this.webhook_url=webhook_url;
        this.created_At=created_At;
    }

}
