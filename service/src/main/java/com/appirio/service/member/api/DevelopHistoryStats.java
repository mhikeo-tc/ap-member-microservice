package com.appirio.service.member.api;

import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Develop history stats
 *
 * Created by rakeshrecharla on 7/22/15.
 */
public class DevelopHistoryStats {

    /**
     * Id of the project
     */
    @Getter
    @Setter
    private Long challengeId;

    /**
     * Component name
     */
    @Getter
    @Setter
    private String challengeName;

    /**
     * Rating date
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= MemberProfileSettings.dateTimePattern, timezone="UTC")
    @Getter
    @Setter
    private Date ratingDate;

    /**
     * New rating
     */
    @Getter
    @Setter
    private Long newRating;
}