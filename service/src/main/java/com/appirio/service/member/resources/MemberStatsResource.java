package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberStats;
import com.appirio.service.member.manager.MemberStatsManager;
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
 * Resource for Member stats
 *
 * Created by rakeshrecharla on 7/8/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/stats")
public class MemberStatsResource {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberStatsResource.class);

    /**
     * Member stats manager
     */
    private MemberStatsManager memberStatsManager;

    /**
     * Constructor to initialize Member stats manager
     * @param memberStatsManager    Member stats manager
     */
    public MemberStatsResource(MemberStatsManager memberStatsManager) {
        this.memberStatsManager = memberStatsManager;
    }

    /**
     * Get member statistics
     * @param handle         Handle of the user
     * @param selector       Field selector
     * @return ApiResponse   Api response
     */
    @GET
    @Timed
    public ApiResponse getMemberStats(@PathParam("handle") String handle,
                                      @APIFieldParam(repClass = MemberStats.class) FieldSelector selector) {

        try {
            logger.debug("getMemberStats, handle : " + handle);
            MemberStats memberStats = memberStatsManager.getMemberStats(handle);
            return ApiResponseFactory.createFieldSelectorResponse(memberStats, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}