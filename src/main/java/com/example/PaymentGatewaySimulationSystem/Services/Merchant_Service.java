package com.example.PaymentGatewaySimulationSystem.Services;

import com.example.PaymentGatewaySimulationSystem.Models.Api_Logs;
import com.example.PaymentGatewaySimulationSystem.Models.Ledger;
import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import com.example.PaymentGatewaySimulationSystem.Models.Payments;
import com.example.PaymentGatewaySimulationSystem.Records.All_Records;
import com.example.PaymentGatewaySimulationSystem.Repositories.ApiLog_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.Ledger_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.Merchant_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.Payments_Repo;
import com.example.PaymentGatewaySimulationSystem.Utils.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Merchant_Service {
    private final Merchant_Repo merchantRepo;
    private final Payments_Repo paymentsRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Ledger_Repo ledgerRepo;
    private final ApiLog_Repo apiLogRepo;


    @Transactional
    public ApiResponse<?> findMerchantByPublicKey(String publicKey,String secretKey,  All_Records.paymentCreation paymentRequest) {

        Merchants merchant=merchantRepo.findByPublicKey(publicKey);
        if(merchant==null){
            return new ApiResponse<>("Merchant Not Found, Invalid PublicKey",false,null);
        }
        if(! passwordEncoder.matches(secretKey,merchant.getSecretKey())){

            Api_Logs apiLogs=new Api_Logs(merchant,"/createPayment","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Unauthorized, Invalid SecretKey",false,null);
        }
        Payments payment=paymentsRepo.findByIdempotencyKey(paymentRequest.idempotencyKey());

        if(payment==null){
            Payments createPayment=new Payments(merchant,
                    paymentRequest.orderId(),
                    paymentRequest.amount(),
                    paymentRequest.currency(),
                    "CREATED",
                    paymentRequest.paymentMethod(),
                    paymentRequest.idempotencyKey(),
                    0,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
           Payments savedPayment= paymentsRepo.save(createPayment);

           All_Records.paymentCreationResponse response=new All_Records.paymentCreationResponse(savedPayment.getId(),
                   savedPayment.getMerchant().getId(),
                   savedPayment.getOrder_id(),
                   savedPayment.getAmount(),
                   savedPayment.getCurrency(),
                   savedPayment.getStatus(),
                   savedPayment.getPayment_method(),
                   savedPayment.getIdempotencyKey(),
                   savedPayment.getCreated_At());

            Api_Logs apiLogs=new Api_Logs(merchant,"/createPayment","POST","SUCCESS",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

           return new ApiResponse<>("Payment Created Successfully",true,response);

        }
        All_Records.paymentCreationResponse response=new All_Records.paymentCreationResponse(payment.getId(),
                payment.getMerchant().getId(),
                payment.getOrder_id(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getPayment_method(),
                payment.getIdempotencyKey(),
                payment.getCreated_At());

        Api_Logs apiLogs=new Api_Logs(merchant,"/createPayment","POST","SUCCESS",LocalDateTime.now());
        apiLogRepo.save(apiLogs);

        return new ApiResponse<>("Payment Already Exist",true,response);

    }


    @Transactional
    @CachePut( value="Payments",key = "'Status'")
    public ApiResponse<?> paymentConfirmation(String publicKey, String secretKey, UUID paymentId) {
        Merchants merchant=merchantRepo.findByPublicKey(publicKey);
        if(merchant==null){
            return new ApiResponse<>("Merchant Not Found, Invalid PublicKey",false,null);
        }

        if(!passwordEncoder.matches(secretKey,merchant.getSecretKey())){

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Invalid SecretKey",false,null);

        }

        Optional<Payments> payment=paymentsRepo.findById(paymentId);
        if(payment.isEmpty()){

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Payment Not Found,Invalid PaymentId",false,null);
        }
        if(! payment.get().getMerchant().getId().equals(merchant.getId())){

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Unauthorized Merchant, You Don't have the Access for Others Payments",false,null);
        }

        if(payment.get().getStatus().equals("SUCCESS")){

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Payment Already Confirmed Successfully",false,true);
        }
        if(payment.get().getStatus().equals("FAILED")){

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Payment Failed, Please Create New Payment",false,null);
        }

        /* Simulating real world payment processing using Random class70% success rate, 30% failure rate */
        Random random=new Random();
        boolean isSuccess=random.nextInt(10)>2;
        if(isSuccess){
            payment.get().setStatus("SUCCESS");
        }
        else {
            payment.get().setStatus("FAILED");
        }
        payment.get().setUpadted_At(LocalDateTime.now());
        payment.get().setVersion(1);
        paymentsRepo.save(payment.get());

        if(payment.get().getStatus().equals("SUCCESS")) {
            Ledger ledger = new Ledger(payment.get(), merchant,payment.get().getAmount(),payment.get().getStatus(),LocalDateTime.now() );
            ledgerRepo.save(ledger);
            All_Records.paymentConfirmationResponse response=new All_Records.paymentConfirmationResponse(payment.get().getId(),payment.get().getOrder_id(),merchant.getId(),payment.get().getStatus(),"YES",payment.get().getAmount(),
                    payment.get().getPayment_method(),payment.get().getCreated_At(),payment.get().getUpadted_At());

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","SUCCESS",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Payment Successfull",true,response);
        }
        else{
            Ledger ledger = new Ledger(payment.get(), merchant, BigDecimal.ZERO.setScale(2),payment.get().getStatus(),LocalDateTime.now() );
            ledgerRepo.save(ledger);
            All_Records.paymentConfirmationResponse response=new All_Records.paymentConfirmationResponse(payment.get().getId(),payment.get().getOrder_id(),merchant.getId(),payment.get().getStatus(),"NO",BigDecimal.ZERO.setScale(2),
                    payment.get().getPayment_method(),payment.get().getCreated_At(),payment.get().getUpadted_At());

            Api_Logs apiLogs=new Api_Logs(merchant,"/PaymentConfirmation","POST","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Payment Failed",false,response);
        }



    }

    public ApiResponse<?> getPayments(String publicKey, String secretKey) {

        Merchants merchant=merchantRepo.findByPublicKey(publicKey);
        if(merchant==null){
            return new ApiResponse<>("Merchant Not Found, Invalid PublicKey",false,null);
        }
        if(!passwordEncoder.matches(secretKey,merchant.getSecretKey())){

            Api_Logs apiLogs=new Api_Logs(merchant,"/getPayments","GET","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("Invalid SecretKey",false,null);
        }
        UUID merchantId=merchant.getId();
        List<Payments> payments=paymentsRepo.findByMerchantId(merchantId);
        if(payments.isEmpty()){

            Api_Logs apiLogs=new Api_Logs(merchant,"/getPayments","GET","FAILED",LocalDateTime.now());
            apiLogRepo.save(apiLogs);

            return new ApiResponse<>("No Payments had been Created by "+merchantId,false,null);
        }
        List<All_Records.getAllPayments>response=payments.stream()
                .map(payment->new All_Records.getAllPayments(payment.getId(),
                        payment.getMerchant().getId(),
                        payment.getOrder_id(),
                        payment.getStatus(),
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getPayment_method(),
                        payment.getIdempotencyKey(),
                        payment.getCreated_At(),
                        payment.getUpadted_At())).toList();

        Api_Logs apiLogs=new Api_Logs(merchant,"/getPayments","GET","SUCCESS",LocalDateTime.now());
        apiLogRepo.save(apiLogs);

        return new ApiResponse<>("Payments History",true,response);
    }
}
