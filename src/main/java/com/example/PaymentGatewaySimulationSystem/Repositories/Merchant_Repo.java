package com.example.PaymentGatewaySimulationSystem.Repositories;

import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Merchant_Repo extends JpaRepository<Merchants, UUID> {

    Merchants findByName(String name);

    Merchants findByPublicKey(String publicKey);
}
