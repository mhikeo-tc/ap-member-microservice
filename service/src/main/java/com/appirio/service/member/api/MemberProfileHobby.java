package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileHobby model
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.1
 *
 * @author thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileHobby implements RESTResource {
    /**
     * The hobby field
     */
    @Getter
    @Setter
    private String hobby;

    /**
     * The description field
     */
    @Getter
    @Setter
    private String description;
}
