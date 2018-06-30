package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileDevice model
 *
 * @author thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileDevice implements RESTResource {
    /**
     * The deviceType field
     */
    @Getter
    @Setter
    private String deviceType;

    /**
     * The manufacturer field
     */
    @Getter
    @Setter
    private String manufacturer;

    /**
     * The model field
     */
    @Getter
    @Setter
    private String model;

    /**
     * The operatingSystem field
     */
    @Getter
    @Setter
    private String operatingSystem;

    /**
     * The osVersion field
     */
    @Getter
    @Setter
    private String osVersion;

    /**
     * The osLanguage field
     */
    @Getter
    @Setter
    private String osLanguage;
}
