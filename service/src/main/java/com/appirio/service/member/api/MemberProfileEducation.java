package com.appirio.service.member.api;

import java.util.Date;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileEducation model 
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileEducation implements RESTResource {
    /**
     * The type field
     */
    @Getter
    @Setter
    private String type;

    /**
     * The schoolCollegeName field
     */
    @Getter
    @Setter
    private String schoolCollegeName;

    /**
     * The major field
     */
    @Getter
    @Setter
    private String major;

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

    /**
     * The graduated field
     */
    @Getter
    @Setter
    private Boolean graduated;
}
