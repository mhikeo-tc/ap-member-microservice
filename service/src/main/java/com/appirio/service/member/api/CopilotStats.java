package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents Copilot stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class CopilotStats {

    /**
     * Total contests
     */
    @Getter
    @Setter
    private Long contests;

    /**
     * Total projects
     */
    @Getter
    @Setter
    private Long projects;

    /**
     * Failures
     */
    @Getter
    @Setter
    private Long failures;

    /**
     * Reposts
     */
    @Getter
    @Setter
    private Long reposts;

    /**
     * Active contests
     */
    @Getter
    @Setter
    private Long activeContests;

    /**
     * Active projects
     */
    @Getter
    @Setter
    private Long activeProjects;

    /**
     * Copilot fulfillment
     */
    @Getter
    @Setter
    private Double fulfillment;
}