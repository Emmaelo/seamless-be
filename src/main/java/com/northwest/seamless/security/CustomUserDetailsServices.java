package com.northwest.seamless.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.repository.EmployeeEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServices implements UserDetailsService{
    
    private final EmployeeEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) 
    throws UsernameNotFoundException {
        EmployeeEntity user = userRepository.findByWorkEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

             var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(
                        "ROLE_" + role.name()))
                .collect(Collectors.toList());
                
                 return User
                .withUsername(user.getWorkEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();

    }

}
