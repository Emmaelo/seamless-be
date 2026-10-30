
package com.northwest.seamless.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTGenerator tokenGenerator;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = getJWTFromRequest(request);

        if (StringUtils.hasText(token)
                && tokenGenerator.validateToken(token)
                && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            String userName =
                    tokenGenerator.getUserNameFromJWT(token);

            List<String> roles =
                    tokenGenerator.getRolesFromJWT(token);

            var authorities = roles.stream()
                    .map(role -> role.startsWith("ROLE_")
                            ? role
                            : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails userDetails = new User(
                    userName,
                    "",
                    authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {

        String bearerToken =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7);
        }

        return null;
    }
}
