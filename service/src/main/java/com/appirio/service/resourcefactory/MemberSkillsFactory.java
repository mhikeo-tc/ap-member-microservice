
package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberSkillsDAO;
import com.appirio.service.member.manager.MemberSkillsManager;
import com.appirio.service.member.resources.MemberSkillsResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.service.supply.resources.SupplyServerConfiguration;
import com.appirio.supply.SupplyException;
import io.dropwizard.setup.Environment;

/**
 * Factory for Member skills
 *
 * @author rrecharla@appirio.com
 */
public class MemberSkillsFactory implements ResourceFactory<MemberSkillsResource> {

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
    public MemberSkillsFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    /**
     * Get MemberSkillsResource object
     * @return MemberSkillsResource     Member skills resource
     * @throws SupplyException          Exception for the supply
     */
    @Override
    public MemberSkillsResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        MemberSkillsDAO memberSkillsDAO = new MemberSkillsDAO(mapper);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);

        final MemberSkillsManager memberSkillsManager = new MemberSkillsManager(
                memberSkillsDAO, memberProfileDAO, config.getFileServiceDomain(), config.getApiVersion());
        return new MemberSkillsResource(memberSkillsManager);
    }
}
