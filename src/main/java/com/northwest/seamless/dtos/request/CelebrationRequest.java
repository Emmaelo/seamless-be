package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CelebrationRequest {
    private String type;
    private String period;

}
