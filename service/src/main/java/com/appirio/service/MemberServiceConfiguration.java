/*
 * Copyright (C) 2016 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service;

import com.appirio.clients.BaseClientConfiguration;
import com.appirio.service.supply.resources.SupplyDatasourceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class used to load the member-service.yaml file
 *
 * <p>
 * v1.1 - TOPCODER - ADD SUGGEST BY HANDLE API FOR MEMBER SERVICE
 * <ol>
 * <li>Added elasticSearchUrl and maxMemberSuggestions properties</li>
 * </ol>
 * </p>
 *
 * @author mdesiderio, TCSASSEMBLER
 * @version 1.1
 */
public class MemberServiceConfiguration extends BaseAppConfiguration {

    /**
     * Datasources
     */
    @Valid
    @NotNull
    @JsonProperty
    private List<SupplyDatasourceFactory> databases = new ArrayList<SupplyDatasourceFactory>();

    @Getter
    @Setter
    private String fileServiceDomain;

    @Getter
    @Setter
    private String dynamoDBUrl;

    @Getter
    @Setter
    private String memberEsIndex;

    @Getter
    @Setter
    private String photoURLDomain;


    /**
     * The URL of elastic search server
     */
    @Getter
    @Setter
    private String elasticSearchUrl;

    /**
     * The maximum number of suggestions to return
     */
    @Getter
    @Setter
    private Integer maxMemberSuggestions;

    /**
     * Url of elasticsearch
     */
    @Getter
    @Setter
    private String elasticsearchUrl;

    /**
     * Default limit elasticsearch
     */
    @Getter
    @Setter
    private Integer elasticsearchDefaultLimit;

    /**
     * Default limit of elasticsearch for admin search
     */
    @Getter
    @Setter
    private Integer elasticsearchDefaultAdminLimit;

    /**
     *Flag to enable cache for nested filter of elasticsearch
     */
    @Getter
    @Setter
    private Boolean elasticsearchCacheNestedFilter;
    
    /**
     * The event bus service client config field
     */
    @Valid
    @NotNull
    @Getter
    @Setter
    private BaseClientConfiguration eventBusServiceClientConfig = new BaseClientConfiguration();
    
    /**
     * The email verification config field
     */
    @Valid
    @NotNull
    @Getter
    @Setter
    private EmailVerificationConfiguration emailVerificationConfig;

    /**
     * Get the data source factory
     *
     * @return Data source factory
     */
    public List<SupplyDatasourceFactory> getDatabases() {
        return databases;
    }

    @NotNull
    @JsonProperty
    @Getter
    @Setter
    private Map<String,String> eventStrategies = new HashMap<>();
    
    

}
