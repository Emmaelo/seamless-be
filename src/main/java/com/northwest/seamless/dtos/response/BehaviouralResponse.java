package com.northwest.seamless.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class BehaviouralResponse {
    private int id;
    private String coreValue;
    private int weight;
    private String selfRating;
    private String reviewerRating;
    private String staffId;
    private String year;

}
