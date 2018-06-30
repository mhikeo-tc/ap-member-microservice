/*
 * Copyright (C) 2016 TopCoder Inc., All Rights Reserved.
 */

package com.appirio.service.resourcefactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.manager.MemberSuggestManager;
import com.appirio.service.member.resources.MemberSuggestResource;
import com.appirio.service.member.util.ESClient;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.supply.SupplyException;
import com.google.common.base.Supplier;
import com.google.gson.GsonBuilder;

import io.dropwizard.setup.Environment;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * The factory to create the member suggest resource.
 * 
 * @author TCSASSEMBLER
 * @version 1.0
 */
public class MemberSuggestFactory implements ResourceFactory<MemberSuggestResource> {
    
    /**
     * Supply server configuration
     */
    private final MemberServiceConfiguration config;

    /**
     * Dropwizard environment
     */
    private final Environment env;

    /**
     * Constructor
     * 
     * @param config   member service configuration
     * @param env      Environment for dropwizard
     */
    public MemberSuggestFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }
  


    /**
     * Get MemberSuggestResource object
     * 
     * @return MemberSuggestResource the member suggest resource
     * @throws SupplyException if any error occurs
     */
    @Override
    public MemberSuggestResource getResourceInstance() throws SupplyException {
        // create jest client
        JestClient client = ESClient.get(config.getElasticSearchUrl());        

        // create resource
        return new MemberSuggestResource(new MemberSuggestManager(client, config.getMaxMemberSuggestions()));
    }

}
