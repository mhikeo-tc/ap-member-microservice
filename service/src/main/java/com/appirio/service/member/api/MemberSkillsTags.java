package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Created by rakeshrecharla on 1/19/16.
 */
public class MemberSkillsTags {


    /**
     * Skill id
     */
    @Getter
    @Setter
    private Long id;

    /**
     * Skill name
     */
    @Getter
    @Setter
    private String name;

    /**
     * Sources
     */
    @Getter
    @Setter
    private Set<String> sources;

    /**
     * Synonyms
     */
    @Getter
    @Setter
    private Set<String> synonyms;

    /**
     * Score
     */
    @Getter
    @Setter
    private Long score;
}