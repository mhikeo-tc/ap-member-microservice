package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileSubscription model
 *
 * @author thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileSubscription implements RESTResource {

    /**
     * The name field
     */
    @Getter
    @Setter
    private String name;
}
