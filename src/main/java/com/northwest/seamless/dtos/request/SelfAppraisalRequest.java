package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelfAppraisalRequest {
    private int objectiveId;
    private int staffSelfRating;
    private String staffComment;

}
