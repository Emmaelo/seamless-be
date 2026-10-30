package com.northwest.seamless.dtos.response;

import java.util.List;

import com.northwest.seamless.enumeration.AccessRoles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse{
    private String firstName;
    private String lastName;
    private String staffId;
    private String jobRole;
    private String token;
    private String managerName;
    private List<AccessRoles> accessRoles;

}
