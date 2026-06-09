package com.example.PaymentGatewaySimulationSystem.Services;

import com.example.PaymentGatewaySimulationSystem.Models.Merchants;
import com.example.PaymentGatewaySimulationSystem.Models.RefreshTokens;
import com.example.PaymentGatewaySimulationSystem.Models.Roles;
import com.example.PaymentGatewaySimulationSystem.Models.Users;
import com.example.PaymentGatewaySimulationSystem.Records.All_Records;
import com.example.PaymentGatewaySimulationSystem.Repositories.Merchant_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.Refresh_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.Roles_Repo;
import com.example.PaymentGatewaySimulationSystem.Repositories.User_Repo;
import com.example.PaymentGatewaySimulationSystem.Utils.ApiResponse;
import com.example.PaymentGatewaySimulationSystem.Utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Public_Service {

    private final User_Repo userRepo;
    private final Merchant_Repo merchantRepo;
    private final Roles_Repo rolesRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Refresh_Repo refreshRepo;
    private final JwtUtil jwtUtil;

    @Transactional
    public Merchants merchantRegister(All_Records.merchantRegister register, String publicKey, String secretKey) {



        String HashedPassword=passwordEncoder.encode(register.password());
        String HashedSecretKey=passwordEncoder.encode(secretKey);

        Roles role=rolesRepo.findByRole("MERCHANT");

        Users user=new Users(register.name()
                ,register.email()
                ,HashedPassword,role
                , LocalDateTime.now());

        Users savedUser=userRepo.save(user);

        Merchants merchant=new Merchants(savedUser,register.name(),publicKey,HashedSecretKey,register.webhook_url(),savedUser.getCreated_At());

         return merchantRepo.save(merchant);

    }

    public String login(All_Records.login login) {
        Users user=userRepo.findByEmail(login.email());
        if(user==null){
            return null;
        }
        if(!passwordEncoder.matches(login.password(),user.getPassword())){
            return null;
        }

       Optional<Roles> role=rolesRepo.findById(user.getRole().getId());

        return role.get().getRole();
    }

    public void saveRefreshToken(String refreshToken, String email) {

        Users user=userRepo.findByEmail(email);

        RefreshTokens saveToken=new RefreshTokens(refreshToken,
                user,
                LocalDateTime.now().plusDays(7),
                true,
                LocalDateTime.now());

        refreshRepo.save(saveToken);
    }

    public ApiResponse<?> refreshAPI(HttpServletRequest request, HttpServletResponse response) {

        /* Get RefreshToken from Cookies */
        Cookie[] cookies=request.getCookies();
        String refreshToken=null;

        if(cookies!=null) {

            for(Cookie cookie:cookies){
                if(cookie.getName().equals("RefreshToken")){
                    refreshToken=cookie.getValue();
                    break;
                }
            }
        }

        /* Check Whether Token exist */
        if(refreshToken==null){
            return new ApiResponse<>("No RefreshToken Found",false,null);
        }

        /* Validate Token */
        if(!jwtUtil.validateToken(refreshToken)){
            return new ApiResponse<>("Token Expired, Please Login Again",false,null);
        }

        /* Check whether Token Exist in DB */
        RefreshTokens oldRefreshToken=refreshRepo.findByToken(refreshToken);
        if(oldRefreshToken==null){
            return new ApiResponse<>("Invalid Token",false,null);
        }

        /* Check Whether Token is Active */
        if(!oldRefreshToken.isActive()){
            return new ApiResponse<>("Already Used Token, Please Login Aagin",false,null);
        }

        /* Revoke Old Token */
        oldRefreshToken.setActive(false);
        refreshRepo.save(oldRefreshToken);

        /* Generate New Tokens */
        String email=jwtUtil.extractEmail(refreshToken);
        Users user=userRepo.findByEmail(email);
        String role=user.getRole().getRole();

        String newAccessToken=jwtUtil.generateAccessToken(email,role);
        String newRefreshToken=jwtUtil.generateRefreshToken(email);

        /* Update new RefreshToken in Cookies */
        Cookie cookie=new Cookie("RefreshToken",newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);

        /* Save new RefreshToken in DB */
        RefreshTokens saveToken=new RefreshTokens(newRefreshToken,
                user,
                LocalDateTime.now().plusDays(7),
                true,
                LocalDateTime.now());
        refreshRepo.save(saveToken);

        return new ApiResponse<>("Token refreshed Successfully",true,"AccessToken: "+newAccessToken);
    }
}
