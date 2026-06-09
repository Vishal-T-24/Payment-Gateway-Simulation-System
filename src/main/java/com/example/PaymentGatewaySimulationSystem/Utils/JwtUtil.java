package com.example.PaymentGatewaySimulationSystem.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "your_super_secret_key_must_be_at_least_32_characters_long";
    private final  long AccessExpiration=1000*60*10;
    private final long RefreshExpiration=1000*60*60*24*7;

    private final Key secretKey= Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

   /* Generate Access Token */
    public  String generateAccessToken(String email, String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("ROLE",role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+AccessExpiration))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();

    }

    /* Generate Refresh Token */
    public String generateRefreshToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+RefreshExpiration))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }

    /* Extract Email */
    public String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /* Validate Token */
    public boolean validateToken(String token){
        try{
            extractEmail(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /* Extract Role */
    public String extractRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("ROLE",String.class);
    }




}
