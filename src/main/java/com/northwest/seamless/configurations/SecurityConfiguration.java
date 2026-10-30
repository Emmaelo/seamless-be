package com.northwest.seamless.configurations;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwest.seamless.security.CustomUserDetailsServices;
import com.northwest.seamless.security.JWTAuthenticationFilter;
import com.northwest.seamless.security.JWTGenerator;
import com.northwest.seamless.security.JwtAuthEntryPoint;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
            JWTAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v1/auth/login"
                                )
                        .permitAll()
                        .anyRequest()
                        .permitAll()
                        // .authenticated()
                    );

        return http.build();

    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(JWTGenerator jwtGenerator) {

        return new JWTAuthenticationFilter(jwtGenerator);
    }

     @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsServices userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

}
