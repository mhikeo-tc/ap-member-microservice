package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberHistoryStatsDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberHistoryStatsManager;
import com.appirio.service.member.resources.MemberHistoryStatsResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member history stats
 *
 * @author rrecharla@appirio.com
 */
public class MemberHistoryStatsFactory implements ResourceFactory<MemberHistoryStatsResource> {

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
    public MemberHistoryStatsFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberHistoryStatsResource object
     * @return MemberHistoryStatsResource   Member history stats resource
     * @throws SupplyException              Exception for the supply
     */
    @Override
    public MemberHistoryStatsResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        MemberHistoryStatsDAO memberHistoryStatsDAO = new MemberHistoryStatsDAO(mapper);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);

        final MemberHistoryStatsManager memberHistoryStatsManager = new MemberHistoryStatsManager(
                memberHistoryStatsDAO, memberProfileDAO);
        return new MemberHistoryStatsResource(memberHistoryStatsManager);
    }
}
