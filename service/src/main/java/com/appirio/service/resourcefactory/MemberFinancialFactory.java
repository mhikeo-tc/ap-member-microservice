package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberFinancialDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberFinancialManager;
import com.appirio.service.member.resources.MemberFinancialResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.DAOFactory;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member financial
 *
 * @author rrecharla@appirio.com
 */
public class MemberFinancialFactory implements ResourceFactory<MemberFinancialResource> {

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
    public MemberFinancialFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberFinancialResource object
     * @return MemberFinancialResource      Member financial resource
     * @throws SupplyException              Exception for the supply
     */
    @Override
    public MemberFinancialResource getResourceInstance() throws SupplyException {
        MemberFinancialDAO memberFinancialDAO = DAOFactory.getInstance().createDAO(MemberFinancialDAO.class);
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);

        final MemberFinancialManager memberFinancialManager = new MemberFinancialManager(
                memberFinancialDAO, memberProfileDAO);
        return new MemberFinancialResource(memberFinancialManager);
    }
}
