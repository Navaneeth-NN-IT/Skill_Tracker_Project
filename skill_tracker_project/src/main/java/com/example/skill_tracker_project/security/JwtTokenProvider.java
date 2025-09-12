
package com.example.skill_tracker_project.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims; // <-- Make sure this is imported
import io.jsonwebtoken.Jwts; // <-- Make sure this is imported
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

    // Get the user's role from the authentication object
    String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining());

    return Jwts.builder()
            .setSubject(username)
            .claim("role", role) // <-- ADD THIS LINE to put the role inside the token
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
}

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Get username from JWT token
    public String getUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Validate JWT Token
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsername(token);
        // We can add more validation here if needed (e.g., check for expiration)
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}