package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberExternalAccounts;
import com.appirio.service.member.manager.MemberExternalAccountsManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Resource for Member external accounts
 *
 * Created by rakeshrecharla on 9/10/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/externalAccounts")
public class MemberExternalAccountsResource {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalAccountsResource.class);

    /**
     * Member external accounts manager
     */
    private MemberExternalAccountsManager memberExternalAccountsManager;

    /**
     * Constructor to initialize Member external accounts manager
     * @param memberExternalAccountsManager     Member external accounts manager
     */
    public MemberExternalAccountsResource(MemberExternalAccountsManager memberExternalAccountsManager) {
        this.memberExternalAccountsManager = memberExternalAccountsManager;
    }

    /**
     * Get member external accounts
     * @param handle            Handle of the user
     * @return ApiResponse      Api Response
     */
    @GET
    @Timed
    public ApiResponse getMemberExternalAccounts(@PathParam("handle") String handle,
                                                 @APIFieldParam(repClass = MemberExternalAccounts.class) FieldSelector selector) {

        try {
            logger.debug("getMemberExternalAccounts, handle : " + handle);
            MemberExternalAccounts memberExternalAccounts = memberExternalAccountsManager.getMemberExternalAccounts(handle);
            return ApiResponseFactory.createFieldSelectorResponse(memberExternalAccounts, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}