
package com.northwest.seamless.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.security.core.Authentication;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTGenerator {

    private static final Key key =
            Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(
            Authentication authentication,
            Long minutes) {

        String userName = authentication.getName();

        // Retrieve all the user's authorities
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        Date now = new Date();

        Date expiry = new Date(
                System.currentTimeMillis() + (minutes * 60 * 1000));

        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("roles", roles)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJWT(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRolesFromJWT(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<?> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toList());
    }

    public String generateNewToken(String email, Long minutes) {

        Date currentDate = new Date();

        Date expireDate = new Date(
                System.currentTimeMillis() + (1000 * 60 * minutes));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (SignatureException ex) {
            log.warn("Token signature is invalid");
            return false;

        } catch (ExpiredJwtException ex) {
            log.warn("Token has expired");
            return false;

        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Token is invalid");
            return false;
        }
    }
}
