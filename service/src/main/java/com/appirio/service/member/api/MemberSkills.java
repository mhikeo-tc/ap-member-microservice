package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Represents Member skills
 *
 * Created by rakeshrecharla on 8/31/15.
 */
public class MemberSkills {

    /**
     * Tag name
     */
    @Getter
    @Setter
    private String tagName;

    /**
     * Hidden
     */
    @Getter
    @Setter
    private Boolean hidden;

    /**
     * Score
     */
    @Getter
    @Setter
    private Double score;

    /**
     * Sources
     */
    @Getter
    @Setter
    private Set<String> sources;
}