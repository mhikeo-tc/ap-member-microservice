package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberDistributionStatsDAO;
import com.appirio.service.member.manager.MemberDistributionStatsManager;
import com.appirio.service.member.resources.MemberDistributionStatsResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member distribution stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class MemberDistributionStatsFactory implements ResourceFactory<MemberDistributionStatsResource> {

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
     * @param config    Configuration for the supply server
     * @param env       Environment for dropwizard
     */
    public MemberDistributionStatsFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberDistributionStatsResource object
     * @return MemberDistributionStatsResource      Member distribution stats resource
     * @throws SupplyException                      Exception for the supply
     */
    @Override
    public MemberDistributionStatsResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        MemberDistributionStatsDAO memberDistributionStatsDAO = new MemberDistributionStatsDAO(mapper);

        final MemberDistributionStatsManager memberDistributionStatsManager = new MemberDistributionStatsManager(memberDistributionStatsDAO);
        return new MemberDistributionStatsResource(memberDistributionStatsManager);
    }
}
