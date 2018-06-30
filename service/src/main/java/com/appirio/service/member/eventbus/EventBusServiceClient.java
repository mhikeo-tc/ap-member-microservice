package com.appirio.service.member.eventbus;

import com.appirio.clients.BaseClient;
import com.appirio.clients.BaseClientConfiguration;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The client to communicate with TC Bus With the REST API.
 * 
 * It's added in Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class EventBusServiceClient extends BaseClient {
    /**
     * The logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(EventBusServiceClient.class);

    /**
     * Constructor
     *
     * @param client the Jersey client
     * @param config the configuration
     */
    public EventBusServiceClient(Client client, BaseClientConfiguration config) {
        super(client, config);
    }
    
    /**
     * Fire event
     *
     * @param eventMessage the eventMessage to use
     */
    public void fireEvent(EventMessage eventMessage) {
        try {
            String url = String.format(this.config.getEndpoint());
            WebTarget target = this.client.target(url);
            final Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
            String authToken = this.config.getAdditionalConfiguration().get("authToken");
            Response response = request.header("Authorization", "Bearer " + authToken).post(Entity.entity(eventMessage.getData(), MediaType.APPLICATION_JSON_TYPE));

            if (response.getStatusInfo().getStatusCode() != HttpStatus.OK_200 && response.getStatusInfo().getStatusCode() != HttpStatus.NO_CONTENT_204) {
                LOGGER.error("Unable to fire event", response);
            } else {
                LOGGER.debug("successfully fired the event");
            }
        }  catch (Exception e) {
            LOGGER.error("Fail to fire event", e);
        }
    }
}
