package com.example.server.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
@Component
public class JwtUtils {
    private final String jwtSecret="Cung_nhau_lam_do_an_tot_nghiep_123456789_Sieu_Bao_Mat";
    private final int jwtExpirationMs=86400000; // 1 ngay
    private Key getSigingKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
                .signWith(getSigingKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUserNameFromJwtToken (String token){
        return Jwts.parserBuilder().setSigningKey(getSigingKey()).build().parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parserBuilder().setSigningKey(getSigingKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
            // TODO: handle exception
        }
    }
}
