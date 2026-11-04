package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EducationRequest {
    private String universityAttended;
    private String degreeObtained;
    private String courseOfStudy;
    private String yearOfGraduation;
    private String yearOfAdmission;
    private String gradeObtained;

}
