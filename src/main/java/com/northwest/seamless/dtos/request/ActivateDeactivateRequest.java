package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivateDeactivateRequest {
    private String action;
    private String staffId;
    private String reason;
}
