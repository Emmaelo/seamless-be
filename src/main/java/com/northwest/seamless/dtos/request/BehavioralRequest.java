package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BehavioralRequest {

    private String coreValue;
    private int weight;
    private int rating;

}
