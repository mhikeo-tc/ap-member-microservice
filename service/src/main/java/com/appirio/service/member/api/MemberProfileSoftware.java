package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileSoftware model
 *
 * @author thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileSoftware implements RESTResource {
    /**
     * The softwareType field
     */
    @Getter
    @Setter
    private String softwareType;

    /**
     * The name field
     */
    @Getter
    @Setter
    private String name;
}
