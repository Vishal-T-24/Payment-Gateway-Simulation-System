package com.example.PaymentGatewaySimulationSystem.Repositories;

import com.example.PaymentGatewaySimulationSystem.Models.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Payments_Repo extends JpaRepository<Payments, UUID> {
    Payments findByIdempotencyKey(String s);

    List<Payments> findByMerchantId(UUID merchantId);
}
