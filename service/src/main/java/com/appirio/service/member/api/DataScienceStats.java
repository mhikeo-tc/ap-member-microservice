package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Data science stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class DataScienceStats {

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
     * Algorithm stats
     */
    @Getter
    @Setter
    @JsonProperty("SRM")
    private SrmStats srm;

    /**
     * Marathon match stats
     */
    @Getter
    @Setter
    @JsonProperty("MARATHON_MATCH")
    protected MarathonMatchStats marathonMatch;
    
    /**
     * Most recent event name
     */
    @Getter
    @Setter
    @JsonInclude(Include.NON_NULL)
    private String mostRecentEventName;

    /**
     * Most recent event date
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=MemberProfileSettings.dateTimePattern, timezone="UTC")
    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    private Date mostRecentEventDate;

    /**
     * Most recent submission
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=MemberProfileSettings.dateTimePattern, timezone="UTC")
    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    private Date mostRecentSubmission;
}