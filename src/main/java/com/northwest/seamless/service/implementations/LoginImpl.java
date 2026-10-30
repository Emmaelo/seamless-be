package com.northwest.seamless.service.implementations;

import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.northwest.seamless.dtos.request.LoginRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.LoginResponse;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.security.JWTGenerator;
import com.northwest.seamless.service.interfaces.LoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginImpl implements LoginService {

    private final EmployeeEntityRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
   

    public ApiResponse<LoginResponse> login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        EmployeeEntity user = userRepository
                .findByWorkEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.isAccountLocked()) {
            return ApiResponse.error("Error. Contact HR");
        }

        if(!user.isStatus()){
            return ApiResponse.error("");
        }


        String jwtToken = jwtGenerator.generateToken(auth, 30L);

        LoginResponse loginResponse = LoginResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(jwtToken)
                .staffId(user.getStaffId())
                .jobRole(user.getJobRole())
                .managerName(user.getManagerName())
                .accessRoles(new ArrayList<>(user.getRoles()))
                .build();

        return  ApiResponse.success("Login successful", loginResponse);
    }

}
