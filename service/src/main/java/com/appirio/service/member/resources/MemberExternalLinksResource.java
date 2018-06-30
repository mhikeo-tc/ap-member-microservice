package com.appirio.service.member.resources;

import com.appirio.service.member.api.ExternalLinkURL;
import com.appirio.service.member.api.MemberExternalLinkData;
import com.appirio.service.member.manager.MemberExternalLinksManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.PostPutRequest;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource for Member external links
 *
 * Created by rakeshrecharla on 10/19/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/externalLinks")
public class MemberExternalLinksResource {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalLinksResource.class);

    /**
     * Member external links manager
     */
    private MemberExternalLinksManager memberExternalLinksManager;

    /**
     * Constructor to initialize Member external links manager
     * @param memberExternalLinksManager     Member external links manager
     */
    public MemberExternalLinksResource(MemberExternalLinksManager memberExternalLinksManager) {
        this.memberExternalLinksManager = memberExternalLinksManager;
    }

    /**
     * Get member external links
     * @param handle            Handle of the user
     * @return ApiResponse      Api response
     */
    @GET
    @Timed
    public ApiResponse getMemberExternalLinks(@PathParam("handle") String handle,
                                              @APIFieldParam(repClass = MemberExternalLinkData.class) FieldSelector selector) {

        try {
            logger.debug("getMemberExternalLinks, handle : " + handle);
            List<MemberExternalLinkData> memberExternalLinkDataList = memberExternalLinksManager.getMemberExternalLinks(handle);
            return ApiResponseFactory.createFieldSelectorResponse(memberExternalLinkDataList, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Create member external link
     * @param handle            Handle of the user
     * @param authUser          Authentication user
     * @param postPutRequest    Post put request
     * @return ApiResponse      Api response
     */
    @POST
    @Timed
    public ApiResponse createMemberExternalLink(@PathParam("handle") String handle, @Auth AuthUser authUser,
                                                @Valid PostPutRequest<ExternalLinkURL> postPutRequest) {

        try {
            logger.debug("createMemberExternalLink, handle : " + handle);
            ExternalLinkURL externalLinkURL = postPutRequest.getParam();
            return ApiResponseFactory.createResponse(memberExternalLinksManager.createMemberExternalLink(
                    handle, authUser, externalLinkURL));
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }

    /**
     * Delete member external link
     * @param handle            Handle of the user
     * @param key               Key
     * @param authUser          Authentication user
     * @return ApiResponse      Api response
     */
    @DELETE
    @Timed
    @Path("{key}")
    public ApiResponse deleteMemberExternalLink(@PathParam("handle") String handle, @PathParam("key") String key,
                                                @Auth AuthUser authUser) {
        try {
            logger.debug("deleteMemberExternalLink, handle : " + handle);
            return ApiResponseFactory.createResponse(memberExternalLinksManager.deleteMemberExternalLink(
                    handle, key, authUser));
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}