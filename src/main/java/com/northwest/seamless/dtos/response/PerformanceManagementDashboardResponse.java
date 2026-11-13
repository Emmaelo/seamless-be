package com.northwest.seamless.dtos.response;

import lombok.Data;

@Data 
public class PerformanceManagementDashboardResponse {
    private int noObjectives = 0;
    private int activeObjectives = 0;
    private int approvedObjectives = 0;
    private int declinedObjectives = 0;
    private int pendingApproval = 0;

    private int completed = 0;
    private int inprogress = 0;
   

    private int zeroTo25 = 0;
    private int twenty5To50 = 0;
    private int fifyTo75 = 0;
    private int seventy5To100 = 0;

    private int counterSignPromote = 0;
    private int counterSignDemote = 0;
    private int counterSignTerminate = 0;
    private int counterSignPlaceOnPip = 0;
    private int counterSignJobRedesign = 0;
    private int counterSignNoChange = 0;
    private int counterSignAppraisalReview = 0;


    private int supervisorPromote = 0;
    private int supervisorDemote = 0;
    private int supervisorTerminate = 0;
    private int supervisorPlaceOnPip = 0;
    private int supervisorJobRedesign = 0;
    private int supervisorNoChange = 0;
    private int supervisorAppraisalReview = 0;


}
