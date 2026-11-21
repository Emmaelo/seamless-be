package com.northwest.seamless.service.implementations;

import java.time.LocalDateTime;
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

import com.northwest.seamless.dtos.request.BehavioralRequest;
import com.northwest.seamless.dtos.request.ObjectiveRequest;
import com.northwest.seamless.dtos.request.SelfAppraisalRequest;
import com.northwest.seamless.dtos.request.SelfRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;
import com.northwest.seamless.entity.appraisal.BehaviouralAssessment;
import com.northwest.seamless.entity.appraisal.Objectives;
import com.northwest.seamless.entity.appraisal.PerformanceReview;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.exceptions.ApplicationException;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.repository.PerformanceReviewRepository;
import com.northwest.seamless.service.interfaces.EmployeeService;
import com.northwest.seamless.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeImpl implements EmployeeService {
    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeEntityRepository employeeEntityRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ApiResponse<String> createObjectives(List<ObjectiveRequest> requests, int performanceReviewId) {

        PerformanceReview review = performanceReviewRepository
                .findById(performanceReviewId)
                .orElseThrow(() -> new ApplicationException("performance review not found"));

        for (ObjectiveRequest request : requests) {

            Objectives objective = Objectives.builder()
                    .title(request.getTitle())
                    .target(request.getTarget())
                    .performanceReview(review)
                    .weight(request.getWeight())
                    .build();

            review.addObjective(objective);
        }
        review.setStatus(ReviewStatus.SUBMITTED);
        review.setSubmittedAt(LocalDateTime.now());

        performanceReviewRepository.save(review);

        return ApiResponse.success("Objectives created successfully");
    }

    public ApiResponse<PerformanceReviewResponse> getEmployeePerformanceReview(String year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<EmployeeEntity> employeeEntity = employeeEntityRepository.findByWorkEmail(email);

        if (employeeEntity.isEmpty()) {
            return ApiResponse.error("User not found");
        }

        EmployeeEntity employee = employeeEntity.get();

        Optional<PerformanceReview> performanceReview = performanceReviewRepository.findByAppraisalYearAndStaffId(year,
                employee.getStaffId());

        if (performanceReview.isEmpty()) {
            return ApiResponse.error("No objectives found");
        }

        PerformanceReview pReview = performanceReview.get();

        return ApiResponse.success("success", modelMapper.map(pReview, PerformanceReviewResponse.class));

    }

    @Transactional
    public ApiResponse<String> appraiseSelf(SelfRequest request) {

        PerformanceReview pReview = performanceReviewRepository
                .findById(request.getPerformanceReviewId()).orElse(null);

        if (pReview == null) {
            return ApiResponse.error("Performance review not found");
        }

        if (pReview.getStatus() != ReviewStatus.APPROVED) {
            return ApiResponse.error("Not up for appraisal");
        }

        if (request.getSelfAppraisalRequests() == null || request.getSelfAppraisalRequests().isEmpty()) {
            return ApiResponse.error("Please provide objective ratings");
        }

        // Build a map of objectives belonging to this review
        Map<Integer, Objectives> objectiveMap = new HashMap<>();

        for (Objectives objective : pReview.getObjectives()) {
            objectiveMap.put(objective.getId(), objective);
        }

        Set<Integer> processedIds = new HashSet<>();

        // STEP 1: Validate all requests without modifying anything
        for (SelfAppraisalRequest rq : request.getSelfAppraisalRequests()) {

            if (!processedIds.add(rq.getObjectiveId())) {
                return ApiResponse.error(
                        "Duplicate objective ID: " + rq.getObjectiveId());
            }

            Objectives objective = objectiveMap.get(rq.getObjectiveId());

            if (objective == null) {
                return ApiResponse.error(
                        "Objective does not belong to this performance review");
            }

            if (rq.getStaffSelfRating() < 1 ||
                    rq.getStaffSelfRating() > 5) {
                return ApiResponse.error(
                        "Self-rating must be between 1 and 5");
            }
        }

        if (processedIds.size() != objectiveMap.size()) {
            return ApiResponse.error(
                    "Please provide ratings for all objectives");
        }

        if (request.getBehavioralRequests().size() != 7) {
            return ApiResponse.error("7 core values must be submitted");
        }

        double objectiveScore = 0.0;
        double behavioralScore = 0.0;

        // STEP 2: Update objectives only after all validations pass
        for (SelfAppraisalRequest rq : request.getSelfAppraisalRequests()) {

            Objectives objective = objectiveMap.get(rq.getObjectiveId());

            objective.setStaffSelfRating(rq.getStaffSelfRating());

            double score = Utils.calculatescore(rq.getStaffSelfRating(), objective.getWeight(), 5);

            objectiveScore = objectiveScore + score;
        }

        for (BehavioralRequest req : request.getBehavioralRequests()) {

            BehaviouralAssessment assessment = new BehaviouralAssessment();
            assessment.setCoreValue(req.getCoreValue());
            assessment.setWeight(req.getWeight());
            assessment.setSelfRating(req.getRating());
            assessment.setYear(pReview.getAppraisalYear());
            assessment.setStaffId(pReview.getStaffId());

            double score = Utils.calculatescore(req.getRating(), req.getWeight(),
                    getBehaviourRating(req.getCoreValue()));

            behavioralScore = behavioralScore + score;

            pReview.addBehaviourAssessment(assessment);

        }

        int finalScore = Utils.calculateFinalScore(objectiveScore, behavioralScore);

        pReview.setStatus(ReviewStatus.SELF_APPRAISED);
        pReview.setEmployeeObjectiveScore((int) Math.round(objectiveScore));
        pReview.setEmployeeBehaviouralScore((int) Math.round(behavioralScore));
        pReview.setEmployeeFinalScore(finalScore);

        performanceReviewRepository.save(pReview);

        return ApiResponse.success(
                "Self-appraisal submitted successfully");
    }

    private int getBehaviourRating(String behavior) {

        if (behavior.equalsIgnoreCase("Integrity")) {
            return 2;

        }
        return 5;

    }

}
