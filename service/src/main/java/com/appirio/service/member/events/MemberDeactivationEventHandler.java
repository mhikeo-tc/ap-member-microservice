package com.appirio.service.member.events;

import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.util.jsonevent.Event;
import com.appirio.eventsbus.api.client.util.jsonevent.EventHandler;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.supply.constants.MemberStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Handles Member deactivation event
 *
 * Created by rakeshrecharla on 12/8/15.
 */
public class MemberDeactivationEventHandler implements EventHandler {

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
    private final Logger logger = LoggerFactory.getLogger(MemberDeactivationEventHandler.class);

    /**
     * Event producer to send errors to dead letter queue
     */
    private EventProducer eventProducer;

    /**
     * Constructor that initializes DAO and producer
     * @param memberProfileDAO
     * @param eventProducer
     */
    public MemberDeactivationEventHandler(MemberProfileDAO memberProfileDAO, EventProducer eventProducer) {
        this.dao = memberProfileDAO;
        this.eventProducer = eventProducer;
    }

    /**
     * Receives member deactivation event and updates status
     */
    @Override
    public void handleEvent(Event memberEvent) {
        try {
            logger.info("Received member deactivation event: " + memberEvent.getPayload());
            JsonNode profile = mapper.readTree(memberEvent.getJsonPayload());

            MemberProfile memberProfile = dao.getMemberProfile(profile.get("handle").asText());

            if(!profile.get("active").asBoolean()) {
                String value = profile.get("status").asText();
                memberProfile.setStatus(MemberStatus.getEnumFromValue(value).toString());
            }
            memberProfile.setUpdatedAt(new Date());
            memberProfile.setUpdatedBy(profile.get("modifiedBy").asText());

            dao.updateMemberProfile(memberProfile , false);
        } catch (Exception e) {
            try {
                ObjectNode error = mapper.createObjectNode();
                error.put("payload", memberEvent.getJsonPayload());
                error.put("message", e.getMessage());
                error.put("type", e.getClass().getName());
                eventProducer.publish("event.user.deactivation.error", memberEvent.getPayload());
            } catch (Exception e1) {
                logger.error(e1.getMessage(), e1);
            }
            logger.error(e.getMessage(), e);
        }
    }
}