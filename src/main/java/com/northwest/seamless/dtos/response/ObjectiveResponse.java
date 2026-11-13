package com.northwest.seamless.dtos.response;


import lombok.Data;

@Data 
public class ObjectiveResponse {
    private int id;
    private String title;
    private String target;
    private int staffSelfRating;
    private int reviewerRating;
    private String staffComment;

}
