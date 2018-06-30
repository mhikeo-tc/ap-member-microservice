package com.appirio.service.resourcefactory;

import io.dropwizard.setup.Environment;

import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.manager.MemberSearchManager;
import com.appirio.service.member.resources.MemberSearchResource;
import com.appirio.service.member.util.ESClient;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.supply.SupplyException;

/**
 * Factory for Member search
 * @author TCSCODER
 */
public class MemberSearchFactory implements ResourceFactory<MemberSearchResource> {


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
    public MemberSearchFactory(MemberServiceConfiguration config, Environment env) {
        this.config = config;
        this.env = env;
    }

    
    /**
     * Get MemberSearchResource object
     * @return MemberSearchResource      Member search resource
     * @throws SupplyException          Exception for the supply
     */
    @Override
    public MemberSearchResource getResourceInstance() throws SupplyException {

        final MemberSearchManager memberSearchManager =
                new MemberSearchManager(
                		ESClient.get(this.config.getElasticSearchUrl()),
                        this.config.getElasticsearchDefaultLimit(),
                        this.config.getElasticsearchDefaultAdminLimit(),
                        this.config.getElasticsearchCacheNestedFilter());
        return new MemberSearchResource(memberSearchManager);
    }
}
