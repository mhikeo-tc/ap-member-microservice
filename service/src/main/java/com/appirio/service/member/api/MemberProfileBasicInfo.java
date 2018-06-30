package com.appirio.service.member.api;

import java.util.Date;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileBasicInfo model
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 *
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileBasicInfo implements RESTResource {
    /**
     * The handle field
     */
    @Getter
    @Setter
    private String handle;

    /**
     * The firstName field
     */
    @Getter
    @Setter
    private String firstName;

    /**
     * The lastName field
     */
    @Getter
    @Setter
    private String lastName;

    /**
     * The shortBio field
     */
    @Getter
    @Setter
    private String shortBio;

    /**
     * The gender field
     */
    @Getter
    @Setter
    private String gender;

    /**
     * The tshirtSize field
     */
    @Getter
    @Setter
    private String tshirtSize;

    /**
     * The country field
     */
    @Getter
    @Setter
    private String country;

    /**
     * The primaryInterestInTopcoder field
     */
    @Getter
    @Setter
    private String primaryInterestInTopcoder;

    /**
     * The birthDate field
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @Getter
    @Setter
    private Date birthDate;

    /**
     * The address field
     */
    @Getter
    @Setter
    private String address;

    /**
     * The state field
     */
    @Getter
    @Setter
    private String state;

    /**
     * The city field
     */
    @Getter
    @Setter
    private String city;

    /**
     * The currentLocation field
     */
    @Getter
    @Setter
    private String currentLocation;

    /**
     * The zipCode field
     */
    @Getter
    @Setter
    private String zipCode;
}
