package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Represents Member search response
 * @author TCSCODER
 */
public class MemberSearch {

    /**
     * Wins
     */
    @Getter
    @Setter
    private Long wins;

    /**
     * Handle
     */
    @Getter
    @Setter
    private String handle;

    /**
     * Id of user
     */
    @Getter
    @Setter
    private Long userId;


    /**
     * Tracks
     */
    @Getter
    @Setter
    private List<String> tracks;

    /**
     * Skills
     */
    @Getter
    @Setter
    private List<MemberSearchSkill> skills;


    /**
     * Url of photo
     */
    @Getter
    @Setter
    private String photoURL;


    /**
     * Country code for competition
     */
    @Getter
    @Setter
    private String competitionCountryCode;



    /**
     * Max rating
     */
    @Getter
    @Setter
    private MaxRating maxRating;


    /**
     * Stats
     */
    @Getter
    @Setter
    private Map<String, Object> stats;


    /**
     * Timestamp when performed the creation of this record
     */
    @Getter
    @Setter
    private Long createdAt;

}