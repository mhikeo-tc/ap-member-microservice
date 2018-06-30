package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileServiceProvider model
 *
 * @author thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileServiceProvider implements RESTResource {
    /**
     * The serviceProviderType field
     */
    @Getter
    @Setter
    private String serviceProviderType;

    /**
     * The name field
     */
    @Getter
    @Setter
    private String name;
}
