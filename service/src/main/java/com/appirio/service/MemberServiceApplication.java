/*
 * Copyright (C) 2016 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service;

import com.appirio.service.member.support.SearchMemberRequestProvider;
import com.appirio.service.member.util.Constants;
import com.appirio.service.resourcefactory.LeaderboardsFactory;
import com.appirio.service.resourcefactory.MemberDistributionStatsFactory;
import com.appirio.service.resourcefactory.MemberExternalAccountsFactory;
import com.appirio.service.resourcefactory.MemberExternalLinksFactory;
import com.appirio.service.resourcefactory.MemberFinancialFactory;
import com.appirio.service.resourcefactory.MemberHistoryStatsFactory;
import com.appirio.service.resourcefactory.MemberProfileFactory;
import com.appirio.service.resourcefactory.MemberProfileTraitsFactory;
import com.appirio.service.resourcefactory.MemberSearchFactory;
import com.appirio.service.resourcefactory.MemberSkillsFactory;
import com.appirio.service.resourcefactory.MemberStatsFactory;
import com.appirio.service.resourcefactory.MemberSuggestFactory;
import com.appirio.service.supply.resources.SupplyDatasourceFactory;

import io.dropwizard.setup.Environment;

import java.util.regex.Pattern;


/**
 * The member service application.
 *
 * <p>
 * v1.1 - TOPCODER - ADD SUGGEST BY HANDLE API FOR MEMBER SERVICE
 * <ol>
 * <li>Updated registerResources method to register the member suggest resource</li>
 * <li>Updated prepare method to fetch the elasticSearchUrl config value from environment variables</li>
 * <li>Updated logServiceSpecificConfigs method to log the elasticSearchUrl and maxMemberSuggestions</li>
 * </ol>
 * </p>
 * 
 * <p>
 * Version 1.2 - Topcoder Member Service - Add Additional Traits v1.0
 * - add the MemberProfileTraitsFactory
 * </p>
 *
 * @author mdesiderio, TCSASSEMBLER
 * @version 1.2 
 */
public class MemberServiceApplication extends BaseApplication<MemberServiceConfiguration> {

    /**
     * Refer to APIApplication
     */
    @Override
    public String getName() {
        return "member-service";
    }


    /**
     * Log service specific configurations.
     *
     * @param config the service configuration
     */
    @Override
    protected void logServiceSpecificConfigs(MemberServiceConfiguration config) {
        for(SupplyDatasourceFactory dbConfig : config.getDatabases()) {
            logger.info("\tJDBI configuration ");
            logger.info("\t\tDatabase config name : " + dbConfig.getDatasourceName());
            logger.info("\t\tOLTP driver class : " + dbConfig.getDriverClass());
            logger.info("\t\tOLTP connection URL : " + dbConfig.getUrl());
            logger.info("\t\tOLTP Authentication user : " + dbConfig.getUser());
        }

        logger.info("\tElastic Search URL : " + config.getElasticSearchUrl());
        logger.info("\tMax Member Suggestions : " + config.getMaxMemberSuggestions());

        logger.info("\r\n");
    }

    /**
     * Application entrypoint. See dropwizard and jetty documentation for more details
     *
     * @param args       arguments to main
     * @throws Exception Generic exception
     */
    public static void main(String[] args) throws Exception {
        try {
            new MemberServiceApplication().run(args);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gives the subclasses an opportunity to register resources
     *
     * @param config the service configuration
     * @param env the environment
     * @throws Exception if any error occurs
     */
    @Override
    protected void registerResources(MemberServiceConfiguration config, Environment env) throws Exception {
        // Register resources here
        env.jersey().register(new MemberFinancialFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberProfileFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberProfileTraitsFactory(config).getResourceInstance());
        env.jersey().register(new MemberStatsFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberHistoryStatsFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberDistributionStatsFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberSkillsFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberExternalAccountsFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberExternalLinksFactory(config, env).getResourceInstance());
        env.jersey().register(new MemberSuggestFactory(config, env).getResourceInstance());

        env.jersey().register(new MemberSearchFactory(config, env).getResourceInstance());
        env.jersey().register(new LeaderboardsFactory(config, env).getResourceInstance());
        logger.info("Services registered");
    }

    /**
     * Gives the subclasses an opportunity to prepare to run, for instance, to setup databases
     *
     * @param config the service configuration
     * @param env the environment
     * @throws Exception if any error occurs
     */
    @Override
    protected void prepare(MemberServiceConfiguration config, Environment env) throws Exception {
        // get configuration from env
        configDatabases(config, config.getDatabases(), env);

        final Pattern envVarPattern = Pattern.compile("\\$\\{(\\w+)\\}");
        config.setDynamoDBUrl(getConfigValueFromEnv(envVarPattern, config.getDynamoDBUrl()));
        logger.info("Dynamodb URL in application: " + config.getDynamoDBUrl());
        if(config.getDynamoDBUrl().isEmpty()) {
                config.setDynamoDBUrl(null);
        }

        config.setAuthDomain(getConfigValueFromEnv(envVarPattern, config.getAuthDomain()));

        Constants.DYNAMODB_URL = config.getDynamoDBUrl();

        config.setMemberEsIndex(getConfigValueFromEnv(envVarPattern, config.getMemberEsIndex()));
        Constants.MEMBER_INDEX_NAME = config.getMemberEsIndex();

        config.setElasticSearchUrl(getConfigValueFromEnv(envVarPattern, config.getElasticSearchUrl()));
        env.jersey().register(new SearchMemberRequestProvider.Binder());
    }
}
