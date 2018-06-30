package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberExternalAccountsManager;
import com.appirio.service.member.resources.MemberExternalAccountsResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member external accounts
 *
 * Created by rakeshrecharla on 9/10/15.
 */
public class MemberExternalAccountsFactory implements ResourceFactory<MemberExternalAccountsResource> {

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
    public MemberExternalAccountsFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberExternalAccountsResource object
     * @return MemberExternalAccountsResource       Member external accounts resource
     * @throws SupplyException                      Exception for the supply
     */
    @Override
    public MemberExternalAccountsResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        MemberExternalAccountsDAO memberExternalAccountsDAO = new MemberExternalAccountsDAO(mapper);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);

        final MemberExternalAccountsManager memberExternalAccountsManager = new MemberExternalAccountsManager(
                memberExternalAccountsDAO, memberProfileDAO);
        return new MemberExternalAccountsResource(memberExternalAccountsManager);
    }
}
