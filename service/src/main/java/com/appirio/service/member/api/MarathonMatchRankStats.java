package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Marathon rank stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class MarathonMatchRankStats {

    /**
     * Rating
     */
    @Getter
    @Setter
    private Long rating;

    /**
     * Competitions
     */
    @Getter
    @Setter
    private Long competitions;

    /**
     * Average rank
     */
    @Getter
    @Setter
    private Long avgRank;

    /**
     * Average num submissions
     */
    @Getter
    @Setter
    private Long avgNumSubmissions;

    /**
     * Best rank
     */
    @Getter
    @Setter
    private Long bestRank;

    /**
     * Top five finishes
     */
    @Getter
    @Setter
    private Long topFiveFinishes;

    /**
     * Top ten finishes
     */
    @Getter
    @Setter
    private Long topTenFinishes;

    /**
     * Rank
     */
    @Getter
    @Setter
    private Long rank;

    /**
     * Percentile
     */
    @Getter
    @Setter
    private Double percentile;

    /**
     * Volatility
     */
    @Getter
    @Setter
    private Double volatility;

    /**
     * Minimum rating
     */
    @Getter
    @Setter
    private Long minimumRating;

    /**
     * Maximum rating
     */
    @Getter
    @Setter
    private Long maximumRating;

    /**
     * Country rank
     */
    @Getter
    @Setter
    private Long countryRank;

    /**
     * School rank
     */
    @Getter
    @Setter
    private Long schoolRank;

    /**
     * Default language;
     */
    @Getter
    @Setter
    private String defaultLanguage;
   
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
    @JsonInclude(Include.NON_NULL)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=MemberProfileSettings.dateTimePattern, timezone="UTC")
    @Getter
    @Setter
    private Date mostRecentEventDate;
}