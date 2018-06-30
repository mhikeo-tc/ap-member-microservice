package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents Develop rank stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class DevelopRankStats {

    /**
     * Rating
     */
    @Getter
    @Setter
    private Long rating;

    /**
     * Active percentile
     */
    @Getter
    @Setter
    private Double activePercentile;

    /**
     * Active rank
     */
    @Getter
    @Setter
    private Long activeRank;

    /**
     * Active country rank
     */
    @Getter
    @Setter
    private Long activeCountryRank;

    /**
     * Active school rank
     */
    @Getter
    @Setter
    private Long activeSchoolRank;

    /**
     * Overall percentile
     */
    @Getter
    @Setter
    private Double overallPercentile;

    /**
     * Overall rank
     */
    @Getter
    @Setter
    private Long overallRank;

    /**
     * Overall country rank
     */
    @Getter
    @Setter
    private Long overallCountryRank;

    /**
     * Overall school rank
     */
    @Getter
    @Setter
    private Long overallSchoolRank;

    /**
     * Volatility
     */
    @Getter
    @Setter
    private Long volatility;

    /**
     * Reliability
     */
    @Getter
    @Setter
    private Double reliability;

    /**
     * Maximum rating
     */
    @Getter
    @Setter
    private Long maxRating;

    /**
     * Minimum rating
     */
    @Getter
    @Setter
    private Long minRating;
}