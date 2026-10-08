package com.staynest.staynest_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access_token_expiration}")
    private Long access_token_expiration;

    @Value("${jwt.refresh_token_expiration}")
    private Long refresh_token_expiration;

    // Generate JWT
    public String generateToken(UserDetails userDetails,Long expiration) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }


    // generate access token
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails,access_token_expiration);
    }

    // generate refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails,refresh_token_expiration);
    }


    // Extract username from JWT
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate JWT
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Check expiration
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // Extract expiration
    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    // Create SecretKey
    private SecretKey getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}