package com.northwest.seamless.service.implementations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.AppraisalCycleResponse;
import com.northwest.seamless.dtos.response.PerformanceManagementDashboardResponse;
import com.northwest.seamless.dtos.response.PerformanceObjectiveResponse;
import com.northwest.seamless.entity.appraisal.AppraisalCycle;
import com.northwest.seamless.entity.appraisal.PerformanceReview;
import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.repository.AppraisalCycleRepository;
import com.northwest.seamless.repository.PerformanceReviewRepository;
import com.northwest.seamless.service.interfaces.PerformanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceImpl implements PerformanceService {
    private final AppraisalCycleRepository appraisalCycleRepository;
    private final PerformanceReviewRepository performanceReviewRepository;
    private final ModelMapper mapper;

    public ApiResponse<List<AppraisalCycleResponse>> getAppraisalList() {

        int currentYear = LocalDate.now().getYear();

        List<AppraisalCycle> appraisalCycles = appraisalCycleRepository.findByAppraisalYearBetween(
                String.valueOf(currentYear - 29),
                String.valueOf(currentYear));

        List<AppraisalCycleResponse> responses = new ArrayList<>();

        for (AppraisalCycle ap : appraisalCycles) {
            responses.add(mapper.map(ap, AppraisalCycleResponse.class));
        }
        return ApiResponse.success("success", responses);

    }

    @Transactional
    public ApiResponse<String> deleteAppraisalCycle(String year) {

        AppraisalCycle appraisalCycle = appraisalCycleRepository.findByAppraisalYear(year).orElse(null);

        if (appraisalCycle == null) {
            return ApiResponse.error(year + " doesn't exist");
        }

        if (!appraisalCycle.isActive()) {
            return ApiResponse.error(year + " appraisal cycle cannot be deleted");
        }

        appraisalCycleRepository.delete(appraisalCycle);

        return ApiResponse.success(year + " deleted");

    }

    @Transactional
    public ApiResponse<String> extendAppraisalCycleEndDate(String year, LocalDate endDate) {
        AppraisalCycle appraisalCycle = appraisalCycleRepository.findByAppraisalYear(year).orElse(null);

        if (appraisalCycle == null) {
            return ApiResponse.error(year + " doesn't exist");
        }

        if (!appraisalCycle.isActive()) {
            return ApiResponse.error(year + " appraisal cycle cannot be extended");
        }

        appraisalCycle.setEndDate(endDate);

        for (PerformanceReview pr : appraisalCycle.getPerformanceReviews()) {
            pr.setEndDate(endDate);
        }

        appraisalCycleRepository.save(appraisalCycle);

        return ApiResponse.success(year + " end date extended");

    }

    public ApiResponse<PerformanceObjectiveResponse> getEmployeePerformanceObjective(String staffId, String year) {

        PerformanceReview performanceReview = performanceReviewRepository.findByAppraisalYearAndStaffId(year, staffId)
                .orElse(null);
        if (performanceReview == null) {
            return ApiResponse.error("Objectives not found");
        }

        return ApiResponse.success("success", mapper.map(performanceReview, PerformanceObjectiveResponse.class));

    }

    public ApiResponse<List<PerformanceObjectiveResponse>> getPerformanceObjectivesPerYear(String year) {

        List<PerformanceReview> performanceReviews = performanceReviewRepository.findByAppraisalYear(year);

        List<PerformanceObjectiveResponse> responses = new ArrayList<>();

        for (PerformanceReview pr : performanceReviews) {
            responses.add(mapper.map(pr, PerformanceObjectiveResponse.class));
        }

        return ApiResponse.success("success", responses);

    }

    public ApiResponse<PerformanceManagementDashboardResponse> getPerformanceManagementDashboardInfo(String year) {

        List<PerformanceReview> performanceReviews = performanceReviewRepository.findByAppraisalYear(year);

        PerformanceManagementDashboardResponse response = new PerformanceManagementDashboardResponse();

        for (PerformanceReview pr : performanceReviews) {

            if (pr.getObjectives().isEmpty()) {
                response.setNoObjectives(response.getNoObjectives() + 1);
            } else {
                response.setActiveObjectives(response.getActiveObjectives() + 1);
            }

            switch (pr.getStatus()) {
                case SUBMITTED ->
                    response.setPendingApproval(response.getPendingApproval() + 1);

                case REJECTED ->
                    response.setDeclinedObjectives(response.getDeclinedObjectives() + 1);

                case APPROVED, COMPLETED, SELF_APPRAISED, REVIEWED ->
                    response.setApprovedObjectives(response.getApprovedObjectives() + 1);

                default -> {

                }
            }

            if (pr.getStatus() == ReviewStatus.COMPLETED) {
                response.setCompleted(response.getCompleted() + 1);
            } else if (pr.getStatus() != ReviewStatus.CREATED) {
                response.setInprogress(response.getInprogress() + 1);
            }

            int score = pr.getSupervisorFinalScore();

            if (score <= 24) {
                response.setZeroTo25(response.getZeroTo25() + 1);
            } else if (score <= 49) {
                response.setTwenty5To50(response.getTwenty5To50() + 1);
            } else if (score <= 74) {
                response.setFifyTo75(response.getFifyTo75() + 1);
            } else if (score <= 100) {
                response.setSeventy5To100(response.getSeventy5To100() + 1);
            }

            if (pr.getCounterSignerSuggestion() != null) {

                switch (pr.getCounterSignerSuggestion()) {
                    case PROMOTE ->
                        response.setCounterSignPromote(
                                response.getCounterSignPromote() + 1);

                    case DEMOTE ->
                        response.setCounterSignDemote(
                                response.getCounterSignDemote() + 1);

                    case TERMINATION ->
                        response.setCounterSignTerminate(
                                response.getCounterSignTerminate() + 1);

                    case PLACE_ON_PIP ->
                        response.setCounterSignPlaceOnPip(
                                response.getCounterSignPlaceOnPip() + 1);

                    case JOB_REDESIGN ->
                        response.setCounterSignJobRedesign(
                                response.getCounterSignJobRedesign() + 1);

                    case NO_CHANGE ->
                        response.setCounterSignNoChange(
                                response.getCounterSignNoChange() + 1);

                    default ->
                        response.setCounterSignAppraisalReview(
                                response.getCounterSignAppraisalReview() + 1);
                }
            }

            if (pr.getReviewerSuggestion() != null) {

                switch (pr.getReviewerSuggestion()) {
                    case PROMOTE ->
                        response.setSupervisorPromote(
                                response.getSupervisorPromote() + 1);

                    case DEMOTE ->
                        response.setSupervisorDemote(
                                response.getSupervisorDemote() + 1);

                    case TERMINATION ->
                        response.setSupervisorTerminate(
                                response.getSupervisorTerminate() + 1);

                    case PLACE_ON_PIP ->
                        response.setSupervisorPlaceOnPip(
                                response.getSupervisorPlaceOnPip() + 1);

                    case JOB_REDESIGN ->
                        response.setSupervisorJobRedesign(
                                response.getSupervisorJobRedesign() + 1);

                    case NO_CHANGE ->
                        response.setSupervisorNoChange(
                                response.getSupervisorNoChange() + 1);

                    default ->
                        response.setSupervisorAppraisalReview(
                                response.getSupervisorAppraisalReview() + 1);
                }
            }

        }
        return ApiResponse.success("success", response);

    }

}
