package com.northwest.seamless.service.implementations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.northwest.seamless.dtos.request.CounterSignRequest;
import com.northwest.seamless.dtos.request.ReviewerAppraisalRequest;
import com.northwest.seamless.dtos.request.ReviewerRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;
import com.northwest.seamless.entity.appraisal.Objectives;
import com.northwest.seamless.entity.appraisal.PerformanceReview;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.repository.PerformanceReviewRepository;
import com.northwest.seamless.service.interfaces.SupervisorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupervisorsImpl implements SupervisorService {
    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeEntityRepository employeeEntityRepository;
    private final ModelMapper modelMapper;

    public ApiResponse<List<PerformanceReviewResponse>> getStaffsObjectives(String role, String year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<EmployeeEntity> employeeEntity = employeeEntityRepository.findByWorkEmail(email);

        if (employeeEntity.isEmpty()) {
            return ApiResponse.error("User not found");
        }

        EmployeeEntity employee = employeeEntity.get();

        List<PerformanceReview> performanceReviews;

        if ("manager".equalsIgnoreCase(role)) {
            performanceReviews = performanceReviewRepository
                    .findByCounterSignersIdAndAppraisalYear(employee.getStaffId(), year);
        } else {
            performanceReviews = performanceReviewRepository.findByReviewerIdAndAppraisalYear(employee.getStaffId(),
                    year);
        }

        List<PerformanceReviewResponse> pResponses = new ArrayList<>();

        for (PerformanceReview pr : performanceReviews) {
            pResponses.add(modelMapper.map(pr, PerformanceReviewResponse.class));
        }

        return ApiResponse.success("success", pResponses);
    }

    // for line manager
    public ApiResponse<String> approveObective(int performanceReviewId) {
        Optional<PerformanceReview> performanceReview = performanceReviewRepository.findById(performanceReviewId);

        if (performanceReview.isEmpty()) {
            return ApiResponse.error("Objective not found");
        }

        PerformanceReview pr = performanceReview.get();

        pr.setStatus(ReviewStatus.APPROVED);

        performanceReviewRepository.save(pr);

        return ApiResponse.success("Approved");

    }


    // for line manager
    public ApiResponse<String> rejectObective(int performanceReviewId) {
        Optional<PerformanceReview> performanceReview = performanceReviewRepository.findById(performanceReviewId);

        if (performanceReview.isEmpty()) {
            return ApiResponse.error("Objective not found");
        }

        PerformanceReview pr = performanceReview.get();

        pr.setStatus(ReviewStatus.REJECTED);

        performanceReviewRepository.save(pr);

        return ApiResponse.success("Rejected");

    }


    @Transactional 
    public ApiResponse<String> appraiseStaff(ReviewerRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        EmployeeEntity employeeEntity = employeeEntityRepository.findByWorkEmail(email).orElse(null);

        if (employeeEntity == null) {
            return ApiResponse.error("User not found");
        }

        PerformanceReview performanceReview = performanceReviewRepository
                .findByIdAndReviewerId(request.getPerformanceReviewId(), employeeEntity.getStaffId()).orElse(null);

        if(performanceReview == null){
            return ApiResponse.error("Performance Review not found");
        }

        if (performanceReview.getStatus() != ReviewStatus.SELF_APPRAISED) {
            return ApiResponse.error("Not up for review yet");
        }

        if (request.getReviewerAppraisalRequest() == null || request.getReviewerAppraisalRequest().isEmpty()) {
            return ApiResponse.error("Please provide objective ratings");
        }

        // Build a map of objectives belonging to this review
        Map<Integer, Objectives> objectiveMap = new HashMap<>();

        for (Objectives objective : performanceReview.getObjectives()) {
            objectiveMap.put(objective.getId(), objective);
        }

        Set<Integer> processedIds = new HashSet<>();

        // STEP 1: Validate all requests without modifying anything
        for (ReviewerAppraisalRequest rq : request.getReviewerAppraisalRequest()) {

            if (!processedIds.add(rq.getObjectiveId())) {
                return ApiResponse.error(
                        "Duplicate objective ID: " + rq.getObjectiveId());
            }

            Objectives objective = objectiveMap.get(rq.getObjectiveId());

            if (objective == null) {
                return ApiResponse.error(
                        "Objective does not belong to this performance review");
            }

            if (rq.getReviewerRating() < 1 ||
                    rq.getReviewerRating() > 5) {
                return ApiResponse.error(
                        "Rating must be between 1 and 5");
            }
        }

        if (processedIds.size() != objectiveMap.size()) {
            return ApiResponse.error(
                    "Please provide ratings for all objectives");
        }

        // STEP 2: Update objectives only after all validations pass
        for (ReviewerAppraisalRequest rq : request.getReviewerAppraisalRequest()) {

            Objectives objective = objectiveMap.get(rq.getObjectiveId());

            objective.setReviewerRating(rq.getReviewerRating());
            
        }

        performanceReview.setStatus(ReviewStatus.REVIEWED);
        performanceReview.setReviewerComment(request.getReviewerComment());
        performanceReview.setReviewedAt(LocalDateTime.now());
        performanceReview.setReviewerSuggestion(request.getSuggestion());

        performanceReviewRepository.save(performanceReview);

        return ApiResponse.success(
                "Staff appraised successfully");

    }




    public ApiResponse<String> counterSign(CounterSignRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

          EmployeeEntity employeeEntity = employeeEntityRepository.findByWorkEmail(email).orElse(null);

        if (employeeEntity == null) {
            return ApiResponse.error("User not found");
        }

          PerformanceReview performanceReview = performanceReviewRepository
                .findByIdAndReviewerId(request.getPerformanceReviewId(), employeeEntity.getStaffId()).orElse(null);

        if(performanceReview == null){
            return ApiResponse.error("Performance Review not found");
        }

        if (performanceReview.getStatus() != ReviewStatus.REVIEWED) {
            return ApiResponse.error("Not ready to counter-sign yet");
        }

        performanceReview.setStatus(ReviewStatus.COMPLETED);
        performanceReview.setCounterSignedAt(LocalDateTime.now());
        performanceReview.setCounterSignerSuggestion(request.getSuggestion());
        performanceReview.setCounterSignersComment(request.getComment());

        performanceReviewRepository.save(performanceReview);

        return ApiResponse.success(
                "Counter signed successfully");

    }

    

}
