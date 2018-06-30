package com.appirio.service.member.api;

import com.appirio.tech.core.api.v3.resource.old.RESTResource;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the MemberProfileCommunities model 
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileCommunities implements RESTResource {
    /**
     * The cognitive field
     */
    @Getter
    @Setter
    private Boolean cognitive;

    /**
     * The blockchain field
     */
    @Getter
    @Setter
    private Boolean blockchain;

    /**
     * The ios field
     */
    @Getter
    @Setter
    private Boolean ios;

    /**
     * The predix field
     */
    @Getter
    @Setter
    private Boolean predix;
}
