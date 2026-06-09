package com.example.PaymentGatewaySimulationSystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users  {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id", referencedColumnName = "id",nullable = false)
    private Roles role;

    @Column(name="created_at")
    private LocalDateTime created_At;

    public Users(String name, String email, String password, Roles role, LocalDateTime created_At){
        this.name=name;
        this.email=email;
        this.password=password;
        this.role=role;
        this.created_At=created_At;
    }





}
