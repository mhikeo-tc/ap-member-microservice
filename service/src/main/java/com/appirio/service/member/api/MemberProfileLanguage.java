package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileLanguage model
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.1
 *
 * @author thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileLanguage implements RESTResource {
    /**
     * The language field
     */
    @Getter
    @Setter
    private String language;

    /**
     * The spokenLevel field
     */
    @Getter
    @Setter
    private String spokenLevel;

    /**
     * The writtenLevel field
     */
    @Getter
    @Setter
    private String writtenLevel;
}
