package com.appirio.service.member.events;

import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.util.jsonevent.Event;
import com.appirio.eventsbus.api.client.util.jsonevent.EventHandler;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Handles Member update event
 *
 * Created by rakeshrecharla on 12/8/15.
 */
public class MemberUpdateEventHandler implements EventHandler {

    /**
     * Member profile data access
     */
    private final MemberProfileDAO dao;

    /**
     * Mapper for json marshalling
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(MemberUpdateEventHandler.class);

    /**
     * Event producer to send errors to dead letter queue
     */
    private EventProducer eventProducer;

    /**
     * Constructor that initializes DAO and producer
     * @param memberProfileDAO
     * @param eventProducer
     */
    public MemberUpdateEventHandler(MemberProfileDAO memberProfileDAO, EventProducer eventProducer) {
        this.dao = memberProfileDAO;
        this.eventProducer = eventProducer;
    }

    /**
     * Receives member updation event and updates handle, email
     */
    @Override
    public void handleEvent(Event memberEvent) {
        try {
            logger.info("Received member updation event: " + memberEvent.getPayload());
            JsonNode profile = mapper.readTree(memberEvent.getJsonPayload());

            MemberProfile memberProfile = dao.getMemberProfile(profile.get("id").asInt());

            memberProfile.setHandle(profile.get("handle").asText());
            memberProfile.setHandleLower(profile.get("handle").asText().toLowerCase());
            memberProfile.setEmail(profile.get("email").asText());
            memberProfile.setUpdatedAt(new Date());
            memberProfile.setUpdatedBy(profile.get("modifiedBy").asText());

            dao.updateMemberProfile(memberProfile , false);
        } catch (Exception e) {
            try {
                ObjectNode error = mapper.createObjectNode();
                error.put("payload", memberEvent.getJsonPayload());
                error.put("message", e.getMessage());
                error.put("type", e.getClass().getName());
                eventProducer.publish("event.user.update.error", memberEvent.getPayload());
            } catch (Exception e1) {
                logger.error(e1.getMessage(), e1);
            }
            logger.error(e.getMessage(), e);
        }
    }
}