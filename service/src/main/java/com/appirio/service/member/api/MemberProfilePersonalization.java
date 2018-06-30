package com.appirio.service.member.api;

import java.util.Date;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfilePersonalization model
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author thomaskranitsas
 * @version 1.0
 *
 */
public class MemberProfilePersonalization implements RESTResource {
    /**
     * The userConsent field
     */
    @Getter
    @Setter
    private Boolean userConsent;
}
