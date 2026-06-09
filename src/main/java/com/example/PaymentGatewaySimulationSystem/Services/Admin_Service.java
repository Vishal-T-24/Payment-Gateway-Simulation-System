package com.example.PaymentGatewaySimulationSystem.Services;

import com.example.PaymentGatewaySimulationSystem.Models.Ledger;
import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import com.example.PaymentGatewaySimulationSystem.Repositories.Ledger_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.Merchant_Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Admin_Service {

    private final Merchant_Repo merchantRepo;
    private final Ledger_Repo ledgerRepo;

    public List<Merchants> getAllMerchants(){

        return merchantRepo.findAll();
    }

    @Cacheable(value = "Ledger",key = "'allLedger'")
    public List<Ledger> getLedger() {
        System.out.println("Data's are Fetached from Database");
        return ledgerRepo.findAll();
    }
}
