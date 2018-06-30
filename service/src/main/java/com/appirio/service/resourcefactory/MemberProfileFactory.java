package com.appirio.service.resourcefactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.eventsbus.api.client.EventConsumer;
import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.util.jsonevent.EventHandler;
import com.appirio.service.MemberServiceConfiguration;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberStatsDAO;
import com.appirio.service.member.eventbus.EventBusServiceClient;
import com.appirio.service.member.events.*;
import com.appirio.service.member.manager.MemberProfileManager;
import com.appirio.service.member.resources.MemberProfileResource;
import com.appirio.service.supply.resources.ResourceFactory;
import com.appirio.supply.SupplyException;
import com.appirio.supply.dataaccess.FileInvocationHandler;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;

/**
 * Factory for Member profile
 * 
 * Version 1.1 - Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0
 * - create MemberProfileManager with more configurations related to email verification
 * 
 * @author rrecharla@appirio.com, TCCoder 
 * @version 1.1 
 */
public class MemberProfileFactory implements ResourceFactory<MemberProfileResource> {

	/**
	 * Logger
	 */
	Logger logger = LoggerFactory.getLogger(MemberProfileFactory.class);

	/**
	 * Supply server configuration
	 */
	private MemberServiceConfiguration config;

	/**
	 * Dropwizard environment
	 */
	private Environment env;

	/**
	 * Simple constructor to initialize Supply server configuration and
	 * environment
	 *  @param config    Configuration for the supply server
	 * @param env       Environment for dropwizard
	 */
	public MemberProfileFactory(MemberServiceConfiguration config, Environment env) {
		this.config = config;
		this.env = env;
	}

	/**
	 * Get MemberProfileResource object
	 * @return MemberProfileResource    Member profile resource
	 * @throws SupplyException          Exception for the supply
	 */
	@Override
	public MemberProfileResource getResourceInstance() throws SupplyException {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        if(config.getDynamoDBUrl() != null && !config.getDynamoDBUrl().isEmpty()) {
            client.withEndpoint(config.getDynamoDBUrl());
        }
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        MemberProfileDAO memberProfileDAO = new MemberProfileDAO(mapper);
        MemberStatsDAO memberStatsDAO = new MemberStatsDAO(mapper);
        FileInvocationHandler fileInvocationHandler = new FileInvocationHandler(config.getFileServiceDomain());
        EventProducer eventProducer = EventProducer.getInstance();

        final Client apiClient = new JerseyClientBuilder(env).build(this.getClass().getName());
        EventBusServiceClient eventBusServiceClient = new EventBusServiceClient(apiClient, this.config.getEventBusServiceClientConfig());
        final MemberProfileManager memberProfileManager = new MemberProfileManager(memberProfileDAO, memberStatsDAO,
                config.getPhotoURLDomain(), fileInvocationHandler, eventProducer, eventBusServiceClient, this.config.getEmailVerificationConfig());
		
        try {
			EventConsumer consumer = EventConsumer.getInstance();
			Map<String, EventHandler> events = new HashMap<>();
			events.put("event.user.created", new MemberCreationEventHandler(memberProfileDAO, eventProducer));
			events.put("event.user.activated", new MemberActivationEventHandler(memberProfileDAO, eventProducer));
			events.put("event.user.deactivated", new MemberDeactivationEventHandler(memberProfileDAO, eventProducer));
			events.put("event.user.updated", new MemberUpdateEventHandler(memberProfileDAO, eventProducer));
			MemberEventsClientManager eventClientManager = new MemberEventsClientManager(consumer, events);
			env.lifecycle().manage(eventClientManager);
		} catch (Exception e) {
			logger.error("Error in initializing communication with events bus " + e.getMessage());
			throw new SupplyException(e);
		}

		return new MemberProfileResource(memberProfileManager);
	}
}
