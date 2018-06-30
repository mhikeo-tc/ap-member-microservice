package com.appirio.service.member.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents Data science history track
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class DataScienceHistoryTrack {

    /**
     * Srm history stats
     */
    @Getter
    @Setter
    @JsonProperty("SRM")
    private SrmMarathonMatchHistoryStats srm;

    /**
     * Marathon match history stats
     */
    @Getter
    @Setter
    @JsonProperty("MARATHON_MATCH")
    protected SrmMarathonMatchHistoryStats marathonMatch;
}