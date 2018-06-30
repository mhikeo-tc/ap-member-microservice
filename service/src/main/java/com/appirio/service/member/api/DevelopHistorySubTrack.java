package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represnets Develop history subtrack
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class DevelopHistorySubTrack {

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
     * Develop history stats
     */
    @Getter
    @Setter
    private List<DevelopHistoryStats> history;
}