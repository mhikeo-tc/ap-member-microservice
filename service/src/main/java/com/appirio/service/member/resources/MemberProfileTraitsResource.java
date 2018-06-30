package com.appirio.service.member.resources;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appirio.service.member.api.MemberProfileTrait;
import com.appirio.service.member.manager.MemberProfileTraitsManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.PostPutRequest;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.auth.Auth;

/**
 * MemberProfileTraitsResource provides the endpoints to manage the member
 * profile trait data
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/traits")
public class MemberProfileTraitsResource {
    /**
     * Member profile traits manager
     */
    private final MemberProfileTraitsManager memberProfileTraitsManager;


    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberProfileTraitsResource.class);

    /**
     * Create MemberProfileTraitsResource
     *
     * @param memberProfileTraitsManager the memberProfileTraitsManager to use
     */
    public MemberProfileTraitsResource(MemberProfileTraitsManager memberProfileTraitsManager) {
        this.memberProfileTraitsManager = memberProfileTraitsManager;
    }

    /**
     * Get member profile
     *
     * @param handle the handle to use
     * @param traitIds the traitIds to use
     * @param authUser the authUser to use
     * @param selector the selector to use
     * @return the ApiResponse result
     */
    @GET
    @Timed
    public ApiResponse getMemberProfile(@PathParam("handle") String handle, @QueryParam("traitIds") List<String> traitIds, @Auth AuthUser authUser,
            @APIFieldParam(repClass = HashMap.class) FieldSelector selector) {
        try {
            logger.debug("getMemberProfileTrait, handle : " + handle + " auth : " + authUser);
            return ApiResponseFactory.createResponse(this.memberProfileTraitsManager.getMemberProfileTrait(handle, authUser, traitIds));
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Create member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param putRequest the putRequest to use
     * @return the ApiResponse result
     */
    @POST
    @Timed
    public ApiResponse createMemberProfileTrait(@PathParam("handle") String handle, @Auth AuthUser authUser,
            @Valid PostPutRequest<List<MemberProfileTrait>> putRequest) {
        try {
            logger.debug("createMemberProfileTrait, handle : " + handle + " auth : " + authUser);
            return ApiResponseFactory
                    .createResponse(this.memberProfileTraitsManager.createMemberProfileTrait(handle, authUser, putRequest.getParam()));
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Update member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param putRequest the putRequest to use
     * @return the ApiResponse result
     */
    @PUT
    @Timed
    public ApiResponse updateMemberProfileTrait(@PathParam("handle") String handle, @Auth AuthUser authUser,
            @Valid PostPutRequest<List<MemberProfileTrait>> putRequest) {
        try {
            logger.debug("updateMemberProfileTrait, handle : " + handle + " auth : " + authUser);
            return ApiResponseFactory
                    .createResponse(this.memberProfileTraitsManager.updateMemberProfileTrait(handle, authUser, putRequest.getParam()));
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Delete member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param selector the selector to use
     * @param putRequest the putRequest to use
     * @return the ApiResponse result
     */
    @DELETE
    @Timed
    public ApiResponse deleteMemberProfileTrait(@PathParam("handle") String handle, @QueryParam("traitIds") List<String> traitIds, @Auth AuthUser authUser) {
        try {
            logger.debug("deleteMemberProfileTrait, handle : " + handle + " auth : " + authUser);
            this.memberProfileTraitsManager.deleteMemberProfileTrait(handle, authUser, traitIds);
            return ApiResponseFactory.createResponse(null);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}
