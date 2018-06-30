package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents Marathon history stats
 *
 * Created by rakeshrecharla on 7/13/15.
 */
public class SrmMarathonMatchHistoryStats {

    @Getter
    @Setter
    private List<DataScienceHistoryStats> history;
}