package com.example.PaymentGatewaySimulationSystem.Records;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class All_Records {

    public record merchantRegister(String name
            ,String email
            ,String password
            , String webhook_url){

    }

    public record response(String publicKey,
                           String secretKey){

    }

    public record merchantDetails(UUID id,
                                  String name,
                                  String email,
                                  String publicKey,
                                  String webhook_url,
                                  LocalDateTime created_at){

    }

    public record paymentCreation(String orderId,
                                  BigDecimal amount,
                                  String currency,
                                  String paymentMethod,
                                  String idempotencyKey){

    }

    public record paymentCreationResponse(UUID id,
                                  UUID merchantId,
                                  String orderId,
                                  BigDecimal amount,
                                  String currency,
                                  String status,
                                  String paymentMethod,
                                  String idempotencyKey,
                                  LocalDateTime createdAt){

    }

    public record paymentConfirmationResponse(UUID PaymentId,
                                              String OrderId,
                                              UUID MerchantId,
                                              String Status,
                                              String AmountCredited,
                                              BigDecimal Amount,
                                              String PaymentMethod,
                                              LocalDateTime CredtedAt,
                                              LocalDateTime UpdatedAt)implements Serializable{

    }
    public record getAllPayments(UUID PaymentId,
                                 UUID MerchantId,
                                 String OrderId,
                                 String Status,
                                 BigDecimal Amount,
                                 String Currency,
                                 String PaymentMethod,
                                 String IdempotencyKey,
                                 LocalDateTime CreatedAt,
                                 LocalDateTime UpdatedAt){

    }
    public record ledgerResponse(UUID id,
                                 UUID MerchantId,
                                 UUID PaymentId,
                                 String Status,
                                 LocalDateTime CreatedAt)implements Serializable {

    }

    public record login(String email,
                        String password){

    }

    public record tokens(String accessToken,
                         String refreshToken){

    }
}
