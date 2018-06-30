package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;


/**
 * Represents Member search skill with name and score only
 * @author TCSCODER
 */
public class MemberSearchSkill {
    /**
     * Skill name
     */
    @Getter
    @Setter
    private String name;

    /**
     * Score
     */
    @Getter
    @Setter
    private Double score;
}