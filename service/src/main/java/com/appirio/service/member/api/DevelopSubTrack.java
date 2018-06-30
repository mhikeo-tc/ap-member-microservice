package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Develop sub track
 *
 * Created by rakeshrecharla on 8/12/15.
 */
public class DevelopSubTrack {

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
     * Rank stats
     */
    @Getter
    @Setter
    private DevelopRankStats rank;

    /**
     * Submission stats
     */
    @Getter
    @Setter
    private DevelopSubmissionStats submissions;
    
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