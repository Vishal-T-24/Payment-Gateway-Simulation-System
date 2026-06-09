package com.example.PaymentGatewaySimulationSystem.Configurations;

import com.example.PaymentGatewaySimulationSystem.Services.CustomUserDetailService;
import com.example.PaymentGatewaySimulationSystem.Utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader=request.getHeader("Authorization");


        if(authHeader!=null && authHeader.startsWith("Bearer ")){

            String token=authHeader.substring(7);
            String email=jwtUtil.extractEmail(token);
            String role=jwtUtil.extractRole(token);


            UserDetails userDetails=customUserDetailService.loadUserByUsername(email);

            if(jwtUtil.validateToken(token)){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_"+role)));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                
            }


        }
        filterChain.doFilter(request,response);

    }
}
