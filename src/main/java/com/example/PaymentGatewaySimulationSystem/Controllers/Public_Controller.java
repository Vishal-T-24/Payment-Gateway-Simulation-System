package com.example.PaymentGatewaySimulationSystem.Controllers;

import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import com.example.PaymentGatewaySimulationSystem.Records.All_Records;
import com.example.PaymentGatewaySimulationSystem.Repositories.Merchant_Repo;
import com.example.PaymentGatewaySimulationSystem.Services.Public_Service;
import com.example.PaymentGatewaySimulationSystem.Utils.ApiResponse;
import com.example.PaymentGatewaySimulationSystem.Utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class Public_Controller {

    private final Public_Service publicService;
    private final Merchant_Repo merchantRepo;
    private final JwtUtil jwtUtil;

    @PostMapping("/registerMerchant")
    public ApiResponse<?> merchantRegister(@RequestBody All_Records.merchantRegister register){

        Merchants existMerchant=merchantRepo.findByName(register.name());
        if(existMerchant!=null){
            return new ApiResponse<>("Registration failed. Merchant already exist with the Same Info",false,null);
        }

        String publicKey="pk_"+ UUID.randomUUID();
        String secretKey="sk_"+UUID.randomUUID();

        Merchants merchant=publicService.merchantRegister(register,publicKey,secretKey);

        if(merchant == null){
            return new ApiResponse<>("Registeration Failed",false,null);
        }

        All_Records.response response=new All_Records.response(publicKey,secretKey);

        return new ApiResponse<>("Merchant registered successfully. Store your keys safely, they won't be shown again.",true,response);
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody All_Records.login login, HttpServletResponse response){
        String role=publicService.login(login);
       if(role!=null){

           String accessToken= jwtUtil.generateAccessToken(login.email(),role);
           String refreshToken= jwtUtil.generateRefreshToken(login.email());

           All_Records.tokens tokens=new All_Records.tokens(accessToken,refreshToken);

           Cookie cookie=new Cookie("RefreshToken",refreshToken);
           cookie.setHttpOnly(true);
           cookie.setPath("/");
           cookie.setMaxAge(60*60*24*7);
           response.addCookie(cookie);

           publicService.saveRefreshToken(refreshToken,login.email());

           return new ApiResponse<>("Login Successfull",true,tokens);


       }else{
           return new ApiResponse<>("Unauthorized User",false,null);
       }
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refreshAPI(HttpServletRequest request,
                                     HttpServletResponse response){
      return   publicService.refreshAPI(request,response);
    }

}
