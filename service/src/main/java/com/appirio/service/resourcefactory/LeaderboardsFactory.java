package com.appirio.service.resourcefactory;

import io.dropwizard.setup.Environment;
import io.searchbox.client.JestClient;

import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.manager.MemberSearchManager;
import com.appirio.service.member.resources.LeaderboardsResource;
import com.appirio.service.member.util.ESClient;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.supply.SupplyException;


/**
 * Factory for Leaderboards search
 *
 * @author TCSCODER
 */
public class LeaderboardsFactory implements ResourceFactory<LeaderboardsResource> {

    /**
     * Supply server configuration
     */
    private MemberServiceConfiguration config;

    /**
     * Dropwizard environment
     */
    private Environment env;

    /**
     * Simple constructor to initialize Supply server configuration and environment
     *
     * @param config
     *            Configuration for the supply server
     * @param env
     *            Environment for dropwizard
     */
    public LeaderboardsFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get LeaderboardsResource object
     *
     * @return LeaderboardsResource Member search resource
     * @throws SupplyException
     *             Exception for the supply
     */
    @Override
    public LeaderboardsResource getResourceInstance() throws SupplyException {
        JestClient client = ESClient.get(config.getElasticSearchUrl());
        final MemberSearchManager memberSearchManager = new MemberSearchManager(
        		client, 
        		this.config.getElasticsearchDefaultLimit(), 
        		this.config.getElasticsearchDefaultAdminLimit(), 
        		this.config.getElasticsearchCacheNestedFilter());
        
        return new LeaderboardsResource(memberSearchManager);
    }
}
