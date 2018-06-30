package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberProfileTraitsDAO;
import com.appirio.service.member.manager.MemberProfileTraitsManager;
import com.appirio.service.member.resources.MemberProfileTraitsResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.supply.SupplyException;

/**
 * MemberProfileTraitsFactory is used to create MemberProfileTraitsResource
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileTraitsFactory implements ResourceFactory<MemberProfileTraitsResource> {
    /**
     * Supply server configuration
     */
    private MemberServiceConfiguration config;

    /**
     * Create MemberProfileTraitsFactory
     *
     * @param config the config to use
     */
    public MemberProfileTraitsFactory(MemberServiceConfiguration config) {
        this.config = config;
    }

    /**
     * Get resource instance
     *
     * @throws SupplyException if any error occurs
     * @return the MemberProfileTraitsResource result
     */
    @Override
    public MemberProfileTraitsResource getResourceInstance() throws SupplyException {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        final MemberProfileTraitsManager memberProfileTraitsManager = new MemberProfileTraitsManager(new MemberProfileTraitsDAO(mapper), 
                new MemberProfileDAO(mapper), EventProducer.getInstance());
        return new MemberProfileTraitsResource(memberProfileTraitsManager);
    }
}
