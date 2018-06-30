package com.appirio.service.member.api;

import java.util.Date;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileOrganization model
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.1
 *
 * @author  thomaskranitsas
 * @version 1.1
 *
 */
public class MemberProfileOrganization implements RESTResource {
    /**
     * The name field
     */
    @Getter
    @Setter
    private String name;

    /**
     * The sector field
     */
    @Getter
    @Setter
    private String sector;

    /**
     * The city field
     */
    @Getter
    @Setter
    private String city;

    /**
     * The timePeriodFrom field
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @Getter
    @Setter
    private Date timePeriodFrom;

    /**
     * The timePeriodTo field
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @Getter
    @Setter
    private Date timePeriodTo;
}
