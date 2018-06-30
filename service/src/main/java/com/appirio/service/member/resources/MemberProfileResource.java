package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.PhotoContentType;
import com.appirio.service.member.api.PhotoTokenContentType;
import com.appirio.service.member.manager.MemberProfileManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.PostPutRequest;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.request.annotation.AllowAnonymous;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Represents the MemberProfileResource used to manage the user profile data
 * 
 * <p>
 * Changes in the version 1.1 (Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0)
 * - modify updateMemeberProfile to send the email verificaiton
 * - add the verifyUserEmail method
 * </p>
 * 
 * <p>
 * Version 1.2 - Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0
 * - add the verifyUserEmail method
 * </p>
 * 
 * <p>
 * Version 1.3 - Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0
 * - update updateMemberProfile to send verification email
 * - add verifyUserEmail method
 * </p>
 * 
 * @author TCCoder
 * @version 1.3 
 *
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}")
public class MemberProfileResource {

    /**
     * Member profile manager
     */
    private MemberProfileManager memberProfileManager;

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberProfileResource.class);

    /**
     * Constructor for Member profile resource
     * @param memberProfileManager      Member profile manager
     */
    public MemberProfileResource(MemberProfileManager memberProfileManager) {
        this.memberProfileManager = memberProfileManager;
    }

    /**
     * Get member profile
     * @param handle            Handle of the user
     * @param selector          selector
     * @param securityContext  the security context
     * @return ApiResponse      Api response
     */
    @GET
    @Timed
    @AllowAnonymous
    public ApiResponse getMemberProfile(@PathParam("handle") String handle,
                                        @APIFieldParam(repClass = MemberProfile.class) FieldSelector selector, @Context SecurityContext
                                                    securityContext) {
        try {
            AuthUser authUser = (AuthUser)securityContext.getUserPrincipal();
            logger.debug("getMemberProfile, handle : " + handle + " auth : " + authUser);
            MemberProfile memberProfile = memberProfileManager.getMemberProfile(handle, authUser);
            logger.info(memberProfile.getEmail());
            if (memberProfile == null) {
                return ApiResponseFactory.createResponse(memberProfile);
            }

            return ApiResponseFactory.createFieldSelectorResponse(memberProfile, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Update profile
     * @param handle       Handle of the user
     * @param authUser     Authentication user
     * @param putRequest   Put request
     * @return ApiResponse Api response
     */
    @PUT
    @Timed
    public ApiResponse updateMemberProfile(@PathParam("handle") String handle, @Auth AuthUser authUser,
                                           @Valid PostPutRequest<MemberProfile> putRequest) {
        try {
            logger.debug("updateMemberProfile, handle : " + handle + " auth : " + authUser);
            MemberProfile memberProfile = putRequest.getParam();
            MemberProfile resultMemberProfile = memberProfileManager.updateMemberProfile(handle, authUser, memberProfile);
            return ApiResponseFactory.createResponse(resultMemberProfile);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
    
    /**
     * Verify user email
     *
     * @param handle the handle to use
     * @param newEmail the newEmail to use
     * @param oldEmail the oldEmail to use
     * @param token the token to use
     * @param authUser the authUser to use
     * @return the ApiResponse result
     */
    @POST
    @Path("/verify")
    @Timed
    public ApiResponse verifyUserEmail(@PathParam("handle") String handle, @QueryParam("newEmail") String newEmail, @QueryParam("oldEmail") String oldEmail, 
            @QueryParam("token") String token, @Auth AuthUser authUser) {
        try {
            logger.debug("verifyUserEmail, handle : " + handle + ", newEmail : " + newEmail + ", oldEmail : " + 
                oldEmail + ", token : " + token + " auth : " + authUser);
            memberProfileManager.verifyUserEmail(handle, authUser, newEmail, oldEmail, token);
            return ApiResponseFactory.createResponse(null);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Generate photo Upload url
     * @param handle       Handle of the user
     * @param authUser     Auth user
     * @return ApiResponse Api response
     */
    @POST
    @Timed
    @Path("photoUploadUrl")
    public ApiResponse generatePhotoUploadUrl(@PathParam("handle") String handle, @Auth AuthUser authUser,
                                              @Valid PostPutRequest<PhotoContentType> postPutRequest) {

        try {
            logger.debug("generatePhotoUploadUrl, handle : " + handle + " auth : " + authUser);
            PhotoContentType photoContentType = postPutRequest.getParam();
            return ApiResponseFactory.createResponse(memberProfileManager.
                    generatePhotoUploadUrl(handle, authUser, photoContentType));
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Update photo
     * @param handle       Handle of the user
     * @param authUser     Auth user
     * @return ApiResponse Api response
     */
    @PUT
    @Timed
    @Path("photo")
    public ApiResponse updatePhoto(@PathParam("handle") String handle, @Auth AuthUser authUser,
                                   @Valid PostPutRequest<PhotoTokenContentType> postPutRequest) {
        try {
            logger.debug("updatePhoto, handle : " + handle + " auth : " + authUser);
            PhotoTokenContentType photoTokenContentType = postPutRequest.getParam();
            String photoURL = memberProfileManager.updatePhoto(handle, authUser, photoTokenContentType);
            return ApiResponseFactory.createResponse(photoURL);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}
