package com.example.PaymentGatewaySimulationSystem.Services;

import com.example.PaymentGatewaySimulationSystem.Models.Users;
import com.example.PaymentGatewaySimulationSystem.Repositories.User_Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final User_Repo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user=userRepo.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("User Not Found With Email "+email);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getRole())
                .build();
    }
}
