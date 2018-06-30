package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberFinancial;
import com.appirio.service.member.manager.MemberFinancialManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource for Member financial information
 *
 * Created by rakeshrecharla on 7/8/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/financial")
public class MemberFinancialResource {

    /**
     * Member financial manager
     */
    private MemberFinancialManager memberFinancialManager;

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberFinancialResource.class);

    /**
     * Constructor to initialize member financial manager
     * @param memberFinancialManager    Member financial manager
     */
    public MemberFinancialResource(MemberFinancialManager memberFinancialManager) {
        this.memberFinancialManager = memberFinancialManager;
    }

    /**
     * Get member financial information
     * @param authUser      Authentication user
     * @param selector      Selector
     * @return              Api response
     */
    @GET
    @Timed
    public ApiResponse getMemberFinancial(@Auth AuthUser authUser, @PathParam("handle") String handle,
                                          @APIFieldParam(repClass = MemberFinancial.class) FieldSelector selector) {

        try {
            logger.debug("getMemberFinancial, handle : " + handle + " auth : " + authUser);
            List<MemberFinancial> memberFinancials = memberFinancialManager.getMemberFinancial(handle, authUser);
            return ApiResponseFactory.createFieldSelectorResponse(memberFinancials, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}