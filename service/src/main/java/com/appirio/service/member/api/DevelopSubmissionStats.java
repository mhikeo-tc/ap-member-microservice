package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents Develop submission stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class DevelopSubmissionStats {

    /**
     * Number of inquiries
     */
    @Getter
    @Setter
    private Long numInquiries;

    /**
     * Submissions
     */
    @Getter
    @Setter
    private Long submissions;

    /**
     * Submission rate
     */
    @Getter
    @Setter
    private Double submissionRate;

    /**
     * Passed screening
     */
    @Getter
    @Setter
    private Long passedScreening;

    /**
     * Screening success rate
     */
    @Getter
    @Setter
    private Double screeningSuccessRate;

    /**
     * Passed review
     */
    @Getter
    @Setter
    private Long passedReview;

    /**
     * Review success rate
     */
    @Getter
    @Setter
    private Double reviewSuccessRate;

    /**
     * Appeals
     */
    @Getter
    @Setter
    private Long appeals;

    /**
     * Appeal success rate
     */
    @Getter
    @Setter
    private Double appealSuccessRate;

    /**
     * Maximum score
     */
    @Getter
    @Setter
    private Double maxScore;

    /**
     * Minimum score
     */
    @Getter
    @Setter
    private Double minScore;

    /**
     * Average score
     */
    @Getter
    @Setter
    private Double avgScore;

    /**
     * Average placement
     */
    @Getter
    @Setter
    private Double avgPlacement;

    /**
     * Win percent
     */
    @Getter
    @Setter
    private Double winPercent;
}
