package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents Develop history track
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class DevelopHistoryTrack {

    /**
     * sub track list
     */
    @Getter
    @Setter
    List<DevelopHistorySubTrack> subTracks;
}