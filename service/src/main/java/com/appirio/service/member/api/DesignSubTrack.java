package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Design sub track
 *
 * Created by rakeshrecharla on 8/12/15.
 */
public class DesignSubTrack {

    /**
     * Id of the sub track
     */
    @Getter
    @Setter
    private Long id;

    /**
     * Name of the sub track
     */
    @Getter
    @Setter
    private String name;

    /**
     * Number of inquiries
     */
    @Getter
    @Setter
    private Long numInquiries;

    /**
     * Number of challenges
     */
    @Getter
    @Setter
    private Long challenges;

    /**
     * Number of wins
     */
    @Getter
    @Setter
    private Long wins;

    /**
     * Win percent
     */
    @Getter
    @Setter
    private Double winPercent;

    /**
     * Average placement
     */
    @Getter
    @Setter
    private Double avgPlacement;

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
     * Most recent event date
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=MemberProfileSettings.dateTimePattern, timezone="UTC")
    @Getter
    @Setter
    private Date mostRecentEventDate;
    
    /**
     * Most Recent submission for this track
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=MemberProfileSettings.dateTimePattern, timezone="UTC")
    @Getter
    @Setter
    @JsonInclude(Include.NON_NULL)
    private Date mostRecentSubmission;
}