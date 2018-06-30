package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents Srm challenge stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class SrmChallengeStats {

    /**
     * Level name
     */
    @Getter
    @Setter
    private String levelName;

    /**
     * Number of challenges
     */
    @Getter
    @Setter
    private Long challenges;

    /**
     * Number of failed challenges
     */
    @Getter
    @Setter
    private Long failedChallenges;
}