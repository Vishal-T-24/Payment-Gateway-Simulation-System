package com.example.PaymentGatewaySimulationSystem.Controllers;

import com.example.PaymentGatewaySimulationSystem.Models.Ledger;
import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import com.example.PaymentGatewaySimulationSystem.Records.All_Records;
import com.example.PaymentGatewaySimulationSystem.Services.Admin_Service;
import com.example.PaymentGatewaySimulationSystem.Utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class Admin_Controller {

    private final Admin_Service adminService;

    @GetMapping("/getAllMerchants")
    public ApiResponse<?> getAllMerchants(){

        List<Merchants>merchants=adminService.getAllMerchants();
        if(merchants.isEmpty()){
            return new ApiResponse<>("No Merchants Registered",false,null);
        }

        List<All_Records.merchantDetails>response=merchants.stream()
                .map(merchant-> new All_Records.merchantDetails(merchant.getId(),merchant.getName(),merchant.getUser().getEmail(),merchant.getPublicKey(),merchant.getWebhook_url(),merchant.getCreated_At()))
                .toList();

            return new ApiResponse<>("Available Merchants",true,response);

    }

    @GetMapping("/getLedger")

    public ApiResponse<?> getLedger(){
        List<Ledger>ledgers=adminService.getLedger();
       if(ledgers.isEmpty()){
           return new ApiResponse<>("No Ledger Data",false,null);
       }
       List<All_Records.ledgerResponse>response=ledgers.stream()
               .map(ledger->new All_Records.ledgerResponse(ledger.getId(),
                       ledger.getMerchant().getId(),
                       ledger.getPayment().getId(),
                       ledger.getStatus(),
                       ledger.getCreatedAt())).toList();
       return new ApiResponse<>("Ledger Data's",true,response);
    }
}
