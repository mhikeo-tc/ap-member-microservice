package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberDistributionStats;
import com.appirio.service.member.manager.MemberDistributionStatsManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.tech.core.api.v3.request.FieldSelector;
import com.appirio.tech.core.api.v3.request.QueryParameter;
import com.appirio.tech.core.api.v3.request.annotation.APIFieldParam;
import com.appirio.tech.core.api.v3.request.annotation.APIQueryParam;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Resource for Member distribution stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/stats/distribution")
public class MemberDistributionStatsResource {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberDistributionStatsResource.class);

    /**
     * Member distribution stats manager
     */
    private MemberDistributionStatsManager memberDistributionStatsManager;

    /**
     * Constructor to initialize Member distribution stats manager
     * @param memberDistributionStatsManager    Member stats distribution manager
     */
    public MemberDistributionStatsResource(MemberDistributionStatsManager memberDistributionStatsManager) {
        this.memberDistributionStatsManager = memberDistributionStatsManager;
    }

    /**
     * Get member distribution statistics
     * @param queryParam     Query parameter
     * @param selector       Field selector
     * @return
     */
    @GET
    @Timed
    public ApiResponse getMemberDistributionStats(@APIQueryParam(repClass = MemberDistributionStats.class) QueryParameter queryParam,
                                                  @APIFieldParam(repClass = MemberDistributionStats.class) FieldSelector selector) {

        try {
            logger.debug("getMemberDistributionStats, filter : " + queryParam.getFilter().getFields());
            MemberDistributionStats memberDistributionStats = memberDistributionStatsManager.getMemberDistributionStats(queryParam);
            return ApiResponseFactory.createFieldSelectorResponse(memberDistributionStats, selector);
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, logger);
        }
    }
}
