package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Srm rank stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class SrmRankStats {

    /**
     * Rating
     */
    @Getter
    @Setter
    private Long rating;

    /**
     * Percentile
     */
    @Getter
    @Setter
    private Double percentile;

    /**
     * Rank
     */
    @Getter
    @Setter
    private Long rank;

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
     * Volatility
     */
    @Getter
    @Setter
    private Double volatility;

    /**
     * Maximum rating
     */
    @Getter
    @Setter
    private Long maximumRating;

    /**
     * Minimum rating
     */
    @Getter
    @Setter
    private Long minimumRating;

    /**
     * Default language
     */
    @Getter
    @Setter
    private String defaultLanguage;

    /**
     * Number of competitions
     */
    @Getter
    @Setter
    private Long competitions;
    
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