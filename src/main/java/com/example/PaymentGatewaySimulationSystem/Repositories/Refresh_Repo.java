package com.example.PaymentGatewaySimulationSystem.Repositories;

import com.example.PaymentGatewaySimulationSystem.Models.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Refresh_Repo extends JpaRepository<RefreshTokens, UUID> {
    RefreshTokens findByToken(String refreshToken);
}
