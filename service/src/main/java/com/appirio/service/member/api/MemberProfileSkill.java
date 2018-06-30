package com.appirio.service.member.api;

import java.util.List;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileSkill model 
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileSkill implements RESTResource {
    /**
     * The design field
     */
    @Getter
    @Setter
    private List<String> design;

    /**
     * The development field
     */
    @Getter
    @Setter
    private List<String> development;

    /**
     * The dataScience field
     */
    @Getter
    @Setter
    private List<String> dataScience;
}
