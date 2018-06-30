package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents Develop stats
 *
 * Created by rakeshrecharla on 7/9/15.
 */
public class DevelopStats {

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
     * Challenge stats
     */
    @Getter
    @Setter
    private List<DevelopSubTrack> subTracks;
    
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