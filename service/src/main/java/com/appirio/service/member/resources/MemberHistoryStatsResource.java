package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberHistoryStats;
import com.appirio.service.member.manager.MemberHistoryStatsManager;
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
 * Resource for Member history stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/{handle}/stats/history")
public class MemberHistoryStatsResource {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberHistoryStatsResource.class);

    /**
     * Member history stats manager
     */
    private MemberHistoryStatsManager memberHistoryStatsManager;


    /**
     * Constructor to initialize Member history stats manager
     * @param memberHistoryStatsManager     Member history stats manager
     */
    public MemberHistoryStatsResource(MemberHistoryStatsManager memberHistoryStatsManager) {
        this.memberHistoryStatsManager = memberHistoryStatsManager;
    }

    /**
     * Get member history statistics
     * @param handle         Handle of the user
     * @param selector       Field selector
     * @return
     */
    @GET
    @Timed
    public ApiResponse getMemberHistoryStats(@PathParam("handle") String handle,
                                      @APIFieldParam(repClass = MemberHistoryStats.class) FieldSelector selector) {

        try {
            logger.debug("getMemberHistoryStats, handle : " + handle);
            MemberHistoryStats memberHistoryStats = memberHistoryStatsManager.getMemberHistoryStats(handle);
            return ApiResponseFactory.createFieldSelectorResponse(memberHistoryStats, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}
