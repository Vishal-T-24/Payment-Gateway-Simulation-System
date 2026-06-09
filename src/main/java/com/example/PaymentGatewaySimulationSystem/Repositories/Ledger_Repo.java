package com.example.PaymentGatewaySimulationSystem.Repositories;

import com.example.PaymentGatewaySimulationSystem.Models.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Ledger_Repo extends JpaRepository<Ledger, UUID> {
}
