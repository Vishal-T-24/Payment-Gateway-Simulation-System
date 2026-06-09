package com.example.PaymentGatewaySimulationSystem.Repositories;

import com.example.PaymentGatewaySimulationSystem.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface User_Repo extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
