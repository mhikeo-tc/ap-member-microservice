package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Data science history stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class DataScienceHistoryStats {

    /**
     * Id of the challenge
     */
    @Getter
    @Setter
    private Long challengeId;

    /**
     * Name of the challenge
     */
    @Getter
    @Setter
    private String challengeName;

    /**
     * Date
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=MemberProfileSettings.dateTimePattern, timezone="UTC")
    @Getter
    @Setter
    private Date date;

    /**
     * Rating
     */
    @Getter
    @Setter
    private Double rating;

    /**
     * Placement
     */
    @Getter
    @Setter
    private Long placement;

    /**
     * Percentile
     */
    @Getter
    @Setter
    private Double percentile;
}
