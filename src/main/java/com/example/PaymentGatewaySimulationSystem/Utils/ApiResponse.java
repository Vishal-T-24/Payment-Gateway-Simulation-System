package com.example.PaymentGatewaySimulationSystem.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T> implements Serializable {

    private String message;
    private boolean success;
    private T data;
}
