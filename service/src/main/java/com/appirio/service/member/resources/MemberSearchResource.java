package com.appirio.service.member.resources;

import com.appirio.service.member.api.MemberSearch;
import com.appirio.service.member.api.MemberSearchRequest;
import com.appirio.service.member.manager.MemberSearchManager;
import com.appirio.supply.ErrorHandler;
import com.appirio.supply.dataaccess.QueryResult;
import com.appirio.tech.core.api.v3.request.annotation.APIQueryParam;
import com.appirio.tech.core.api.v3.request.annotation.AllowAnonymous;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.api.v3.response.ApiResponseFactory;
import com.appirio.tech.core.api.v3.response.Result;
import com.appirio.tech.core.auth.AuthUser;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resource for Member search
 * migrate the member search functionality written in node and running in Lambda to member service.
 * Here is the source for the lambda node.js function: https://github.com/appirio-tech/tc-lambda-members/blob/dev/src/index.js
 * @author TCSCODER
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("members/_search")
public class MemberSearchResource {

    /**
     * The totalCount key for metadata
     */
    private static final String TOTAL_COUNT = "totalCount";

    /**
     * Logger for the class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberSearchResource.class);

    /**
     * Member search manager
     */
    private MemberSearchManager memberSearchManager;

    /**
     * Constructor to initialize Member search manager
     * @param memberSearchManager   Member search manager
     */
    public MemberSearchResource(MemberSearchManager memberSearchManager) {
        this.memberSearchManager = memberSearchManager;
    }

    /**
     * Get member search result
     * @param request       Member Search Request @APIQueryParam(repClass = MemberSearch.class)
     *                      @APIQueryParam(repClass = MemberSearchRequest.class)
     * @param securityContext  the security context
     * @return ApiResponse   Api response
     */
    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowAnonymous
    public ApiResponse searchMember( @APIQueryParam(repClass = MemberSearch.class) MemberSearchRequest request,
            @Context SecurityContext
                                                 securityContext) {

        try {
            AuthUser authUser = (AuthUser)securityContext.getUserPrincipal();
            QueryResult<List<Object>> queryResult = memberSearchManager.searchMembers(request, authUser);
            ApiResponse response = ApiResponseFactory.createResponse(queryResult.getData());
            Result result = response.getResult();
            Map<String, Integer> metadata = new HashMap<>();
            metadata.put(TOTAL_COUNT, queryResult.getRowCount());
            response.setResult(result.getSuccess(), result.getStatus(), metadata, result.getContent());
            return response;
        } catch (Throwable ex) {
            return ErrorHandler.handle(ex, LOGGER);
        }
    }
}