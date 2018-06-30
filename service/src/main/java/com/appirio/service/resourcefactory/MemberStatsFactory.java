package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberStatsDAO;
import com.appirio.service.member.manager.MemberStatsManager;
import com.appirio.service.member.resources.MemberStatsResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member stats
 *
 * @author rrecharla@appirio.com
 */
public class MemberStatsFactory implements ResourceFactory<MemberStatsResource> {

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
    public MemberStatsFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberStatsResource object
     * @return MemberStatsResource      Member stats resource
     * @throws SupplyException          Exception for the supply
     */
    @Override
    public MemberStatsResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        MemberStatsDAO memberStatsDAO = new MemberStatsDAO(mapper);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);

        final MemberStatsManager memberStatsManager = new MemberStatsManager(memberStatsDAO, memberProfileDAO);
        return new MemberStatsResource(memberStatsManager);
    }
}
