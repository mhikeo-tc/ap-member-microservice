/*
 * Copyright (C) 2016 TopCoder Inc., All Rights Reserved.
 */

package com.appirio.service.member.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.appirio.service.member.util.Helper;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appirio.service.member.api.MemberSuggest;
import com.appirio.service.member.manager.MemberSuggestManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;



/**
 * The resource to return the member suggestions.
 *
 * @author TCSASSEMBLER
 * @version 1.0
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/_suggest/{term}")
public class MemberSuggestResource {

    /**
     * Logger for the class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberSuggestResource.class);

    /**
     * The allowed roles.
     */
    private static final String[] ALLOWED_ROLES = new String[] {
        "administrator",
        "admin",
        "connect manager",
        "connect admin"
    };

    /**
     * The member suggest manager
     */
    private final MemberSuggestManager memberSuggestManager;


    /**
     * Constructor
     *
     * @param memberSuggestManager the member suggest manager
     */
    public MemberSuggestResource(MemberSuggestManager memberSuggestManager) {
        this.memberSuggestManager = memberSuggestManager;
    }


    /**
     * Get member suggests
     *
     * @param term the prefix of handle
     * @return the api response
     */
    @GET
    @Timed
    public ApiResponse getMemberSuggests(@PathParam("term") String term, @Auth AuthUser authUser) {
        try {
            Helper.checkRole(authUser, ALLOWED_ROLES);
            LOGGER.debug("getMemberSuggests, term : " + term);

            List<MemberSuggest> suggests = memberSuggestManager.getMemberSuggests(term);
            return ApiResponseFactory.createResponse(suggests);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, LOGGER);
        }
    }
}
