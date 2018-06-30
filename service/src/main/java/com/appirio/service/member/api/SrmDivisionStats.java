package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents Srm division stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class SrmDivisionStats {

    /**
     * Level name
     */
    @Getter
    @Setter
    private String levelName;

    /**
     * Problems submitted
     */
    @Getter
    @Setter
    private Long problemsSubmitted;

    /**
     * Problems failed
     */
    @Getter
    @Setter
    private Long problemsFailed;

    /**
     * Problem system by test
     */
    @Getter
    @Setter
    private Long problemsSysByTest;
}