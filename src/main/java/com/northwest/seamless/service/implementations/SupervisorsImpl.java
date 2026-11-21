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
import com.northwest.seamless.dtos.request.ReviewerBehavioralRequest;
import com.northwest.seamless.dtos.request.ReviewerRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;
import com.northwest.seamless.entity.appraisal.BehaviouralAssessment;
import com.northwest.seamless.entity.appraisal.Objectives;
import com.northwest.seamless.entity.appraisal.PerformanceReview;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.enumeration.Suggestion;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.repository.PerformanceReviewRepository;
import com.northwest.seamless.service.interfaces.SupervisorService;
import com.northwest.seamless.utils.Utils;

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

        if ("reviewer".equalsIgnoreCase(role)) {
            performanceReviews = performanceReviewRepository.findByReviewerIdAndAppraisalYear(employee.getStaffId(),
                    year);
        } else {
            performanceReviews = performanceReviewRepository
                    .findByCounterSignersIdAndAppraisalYear(employee.getStaffId(), year);
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

        if (pr.getStatus() != ReviewStatus.SUBMITTED) {
            return ApiResponse.error("You cannot approve the objective at the moment");
        }

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

        if (pr.getStatus() != ReviewStatus.SUBMITTED) {
            return ApiResponse.error("You cannot reject the objective at the moment");
        }

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

        if (performanceReview == null) {
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

        double objectiveScore = 0.0;
        double behavioralScore = 0.0;

        // STEP 2: Update objectives only after all validations pass
        for (ReviewerAppraisalRequest rq : request.getReviewerAppraisalRequest()) {

            Objectives objective = objectiveMap.get(rq.getObjectiveId());

            objective.setReviewerRating(rq.getReviewerRating());

            double score = Utils.calculatescore(rq.getReviewerRating(), objective.getWeight(), 5);

            objectiveScore += score;

        }

        List<BehaviouralAssessment> assessments = performanceReview.getAssessments();

        for (ReviewerBehavioralRequest req : request.getBehavioralRequests()) {

            BehaviouralAssessment assessment = assessments.stream()
                    .filter(a -> a.getId() == req.getId())
                    .findFirst()
                    .orElse(null);

            if (assessment == null) {
                return ApiResponse.error(
                        "Invalid behavioural assessment ID: " + req.getId());
            }

            int maxRating = getBehaviourRating(assessment.getCoreValue());

            double score = Utils.calculatescore(req.getRating(), assessment.getWeight(), maxRating);

            assessment.setReviewerRating(req.getRating());
            behavioralScore += score;

        }

        int finalScore = Utils.calculateFinalScore(objectiveScore, behavioralScore);

        performanceReview.setStatus(ReviewStatus.REVIEWED);
        performanceReview.setReviewerComment(request.getReviewerComment());
        performanceReview.setReviewedAt(LocalDateTime.now());
        performanceReview.setReviewerSuggestion(Suggestion.valueOf(request.getSuggestion().toUpperCase()));
        performanceReview.setSupervisorObjectiveScore((int) Math.round(objectiveScore));
        performanceReview.setSupervisorBehaviouralScore((int) Math.round(behavioralScore));
        performanceReview.setSupervisorFinalScore(finalScore);

        performanceReviewRepository.save(performanceReview);

        return ApiResponse.success(
                "Staff appraised successfully");

    }

    public ApiResponse<String> counterSign(CounterSignRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        EmployeeEntity employeeEntity = employeeEntityRepository.findByWorkEmail(email).orElse(null);

        if (employeeEntity == null) {
            return ApiResponse.error("User not found");
        }

        PerformanceReview performanceReview = performanceReviewRepository
                .findByIdAndReviewerId(request.getPerformanceReviewId(), employeeEntity.getStaffId()).orElse(null);

        if (performanceReview == null) {
            return ApiResponse.error("Performance Review not found");
        }

        if (performanceReview.getStatus() != ReviewStatus.REVIEWED) {
            return ApiResponse.error("Not ready to counter-sign yet");
        }

        performanceReview.setStatus(ReviewStatus.COMPLETED);
        performanceReview.setCounterSignedAt(LocalDateTime.now());
        performanceReview.setCounterSignerSuggestion(Suggestion.valueOf(request.getSuggestion()));
        performanceReview.setCounterSignersComment(request.getComment());

        performanceReviewRepository.save(performanceReview);

        return ApiResponse.success(
                "Counter signed successfully");

    }

    private int getBehaviourRating(String behavior) {

        if (behavior.equalsIgnoreCase("Integrity")) {
            return 2;

        }
        return 5;

    }

}
