package com.example.PaymentGatewaySimulationSystem.Controllers;

import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import com.example.PaymentGatewaySimulationSystem.Models.Payments;
import com.example.PaymentGatewaySimulationSystem.Records.All_Records;
import com.example.PaymentGatewaySimulationSystem.Repositories.Merchant_Repo;
import com.example.PaymentGatewaySimulationSystem.Services.Merchant_Service;
import com.example.PaymentGatewaySimulationSystem.Utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant")
public class Merchant_Controller {

    private final Merchant_Service merchantService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/createPayment")
    public ApiResponse<?> paymentCreation(@RequestHeader("publicKey")String publicKey, @RequestHeader("secretKey")String secretKey, @RequestBody All_Records.paymentCreation paymentRequest){
        return merchantService.findMerchantByPublicKey(publicKey,secretKey,paymentRequest);

    }

    @PostMapping("/paymentConfirmation/{paymentId}")
    public ApiResponse<?> paymentConfirmation(@RequestHeader("publicKey")String publicKey,
                                              @RequestHeader("secretKey")String secretKey,
                                              @PathVariable("paymentId") UUID paymentId){

        return merchantService.paymentConfirmation(publicKey,secretKey,paymentId);

    }

    @GetMapping("/getPayments")
    public ApiResponse<?> getPayments(@RequestHeader("publicKey")String publicKey,
                                      @RequestHeader("secretKey")String secretKey){
        return merchantService.getPayments(publicKey,secretKey);
    }
}
