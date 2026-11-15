package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class ChangeSupervisorRequest {
    private String supervisorId;
    private String action;
    private int performanceId;
}
