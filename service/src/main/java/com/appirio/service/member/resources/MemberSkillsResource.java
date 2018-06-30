package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberAggregatedSkills;
import com.appirio.service.member.api.MemberEnteredSkills;
import com.appirio.service.member.manager.MemberSkillsManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.PostPutRequest;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.PATCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Resource for Member skills
 *
 * Created by rakeshrecharla on 7/8/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/skills")
public class MemberSkillsResource {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberStatsResource.class);

    /**
     * Member skills manager
     */
    private MemberSkillsManager memberSkillsManager;

    /**
     * Constructor to initialize Member skills manager
     * @param memberSkillsManager       Member skills manager
     */
    public MemberSkillsResource(MemberSkillsManager memberSkillsManager) {
        this.memberSkillsManager = memberSkillsManager;
    }

    /**
     * Get member skills
     * @param handle            Handle of the user
     * @param selector          Field selector
     * @return ApiResponse      Api response
     */
    @GET
    @Timed
    public ApiResponse getMemberAggregatedSkills(@PathParam("handle") String handle,
                                                 @APIFieldParam(repClass = MemberAggregatedSkills.class)
                                                 FieldSelector selector) {

        try {
            logger.debug("getMemberAggregatedSkills, handle : " + handle);
            MemberAggregatedSkills memberAggregatedSkills = memberSkillsManager.getMemberAggregatedSkills(handle);
            return ApiResponseFactory.createFieldSelectorResponse(memberAggregatedSkills, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Update member skills
     * @param handle       Handle of the user
     * @param authUser     Authentication user
     * @param putRequest   put request object
     * @return ApiResponse Api response
     */
    @PATCH
    @Timed
    public ApiResponse updateMemberSkills(@PathParam("handle") String handle, @Auth AuthUser authUser,
                                          @Valid PostPutRequest<MemberEnteredSkills> putRequest) {

        try {
            logger.debug("updateMemberSkills, handle : " + handle + " auth : " + authUser);
            MemberEnteredSkills memberEnteredSkills = putRequest.getParam();
            MemberAggregatedSkills memberAggregatedSkills = memberSkillsManager.updateMemberSkills(
                    handle, authUser, memberEnteredSkills);
            return ApiResponseFactory.createResponse(memberAggregatedSkills);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}