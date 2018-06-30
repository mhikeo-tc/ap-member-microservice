package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberExternalLinksDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberExternalLinksManager;
import com.appirio.service.member.resources.MemberExternalLinksResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member external links
 *
 * Created by rakeshrecharla on 10/19/15.
 */
public class MemberExternalLinksFactory implements ResourceFactory<MemberExternalLinksResource> {

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
    public MemberExternalLinksFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberExternalLinksResource object
     * @return MemberExternalLinksResource      Member external links resource
     * @throws SupplyException                  Exception for the supply
     */
    @Override
    public MemberExternalLinksResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        DynamoDB dynamoDB = new DynamoDB(client);

        MemberExternalLinksDAO memberExternalLinksDAO = new MemberExternalLinksDAO(mapper, dynamoDB);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);

        final MemberExternalLinksManager memberExternalLinksManager = new MemberExternalLinksManager(
                memberExternalLinksDAO, memberProfileDAO);
        return new MemberExternalLinksResource(memberExternalLinksManager);
    }
}
