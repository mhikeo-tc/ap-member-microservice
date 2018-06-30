package com.appirio.service.member.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.exception.EmptyEventException;
import com.appirio.eventsbus.api.client.exception.EncodingEventException;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.MemberProfileBasicInfo;
import com.appirio.service.member.api.MemberProfileTrait;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberProfileTraitsDAO;
import com.appirio.service.member.dao.validation.MemberProfileTraitValidator;
import com.appirio.supply.Messages;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import com.appirio.tech.core.auth.AuthUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * MemberProfileTraitsManager is used to manage the member profile trait data
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileTraitsManager {
    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberProfileTraitsManager.class);


    /**
     * DAO for Member profile traits
     */
    private final MemberProfileTraitsDAO memberProfileTraitsDAO;


    /**
     * DAO for Member profile
     */
    private final MemberProfileDAO memberProfileDAO;

    /**
     * Kafka Event Producer
     */
    private EventProducer eventProducer;
    
    /**
     * Create MemberProfileTraitsManager
     *
     * @param memberProfileTraitsDAO the memberProfileTraitsDAO to use
     * @param memberProfileDAO the memberProfileDAO to use
     */
    public MemberProfileTraitsManager(MemberProfileTraitsDAO memberProfileTraitsDAO, MemberProfileDAO memberProfileDAO, EventProducer eventProducer) {
        this.memberProfileTraitsDAO = memberProfileTraitsDAO;
        this.memberProfileDAO = memberProfileDAO;
        this.eventProducer = eventProducer;
    }

    /**
     * Get member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param memberProfileTraitIds the memberProfileTraitIds to use
     * @throws SupplyException if any error occurs
     * @return the List<MemberProfileTrait> result
     */
    public List<MemberProfileTrait> getMemberProfileTrait(String handle, AuthUser authUser, List<String> memberProfileTraitIds) throws SupplyException {
        logger.debug("enter of the getMemberProfileTrait");
        long userId = this.validateUser(handle, authUser, null);
        List<MemberProfileTrait> result = this.memberProfileTraitsDAO.getMemberProfile(userId);
        result.forEach(item -> {
            item.setUserId(null);
        });
        
        if (memberProfileTraitIds == null || memberProfileTraitIds.isEmpty()) {
            return result;
        }
        List<MemberProfileTrait> items = new ArrayList<MemberProfileTrait>();
        List<String> notExistsTraitIds = new ArrayList<String>(memberProfileTraitIds);
        result.forEach(item -> {
            for (String traitId : memberProfileTraitIds) {
                if (item.getTraitId().equals(traitId)) {
                    items.add(item);
                    notExistsTraitIds.remove(traitId);
                }
            }
        });
        
        if (notExistsTraitIds.size() > 0) {
            throw new SupplyException("These trait ids does not exist for the user:" + notExistsTraitIds, HttpServletResponse.SC_NOT_FOUND);
        }
        return items;
    }

    /**
     * Create member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param memberProfileTraits the memberProfileTraits to use
     * @throws IllegalAccessException if any error occurs
     * @throws InvocationTargetException if any error occurs
     * @throws InstantiationException if any error occurs
     * @throws SupplyException if any error occurs
     * @throws NoSuchMethodException if any error occurs
     * @return the List<MemberProfileTrait> result
     */
    public List<MemberProfileTrait> createMemberProfileTrait(String handle, AuthUser authUser, List<MemberProfileTrait> memberProfileTraits)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {
        logger.debug("enter of the createMemberProfileTrait");
        long userId = this.validateUser(handle, authUser, memberProfileTraits);
        
        List<MemberProfileTrait> result = this.memberProfileTraitsDAO.createMemberProfileTrait(userId, memberProfileTraits, Long.parseLong(authUser.getUserId().getId()));
        ObjectMapper mapper = new ObjectMapper();
        this.publishKafkaEvent(mapper.valueToTree(result), "member-profile-trait-create");
        result.forEach(item -> {
            item.setUserId(null);
        });
        return result;
    }

    /**
     * Update member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param memberProfileTraits the memberProfileTraits to use
     * @throws IllegalAccessException if any error occurs
     * @throws InvocationTargetException if any error occurs
     * @throws InstantiationException if any error occurs
     * @throws SupplyException if any error occurs
     * @throws NoSuchMethodException if any error occurs
     * @return the List<MemberProfileTrait> result
     */
    public List<MemberProfileTrait> updateMemberProfileTrait(String handle, AuthUser authUser, List<MemberProfileTrait> memberProfileTraits)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {
        logger.debug("enter of the updateMemberProfileTrait");
        long userId = this.validateUser(handle, authUser, memberProfileTraits);
        List<MemberProfileTrait> result = this.memberProfileTraitsDAO.updateMemberProfileTrait(userId, memberProfileTraits, Long.parseLong(authUser.getUserId().getId()));
        ObjectMapper mapper = new ObjectMapper();
        this.publishKafkaEvent(mapper.valueToTree(result), "member-profile-trait-update");
        result.forEach(item -> {
            item.setUserId(null);
        });
        return result;
    }

    /**
     * Delete member profile trait
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param memberProfileTraitIds the memberProfileTraitIds to use
     * @throws SupplyException if any error occurs
     */
    public void deleteMemberProfileTrait(String handle, AuthUser authUser, List<String> memberProfileTraitIds) throws SupplyException {
        logger.debug("enter of the deleteMemberProfileTrait");
        long userId = this.validateUser(handle, authUser, null);
        this.memberProfileTraitsDAO.deleteMemberProfileTrait(userId, memberProfileTraitIds);
    }
    
    /**
     * fireKafkaEvent publishes profile trait events on to the kafka bus
     *
     * @param json Json node object
     * @param topic Topic name
     */
    private void publishKafkaEvent(JsonNode json, String topic) {
        // fire an event on to the kafka bus
        try {
            eventProducer.publish(topic, json);
        } catch (EmptyEventException e) {
            logger.info("Failed to publish message " + e.getMessage());
        } catch (EncodingEventException e) {
            logger.info("Event Encoding Error " + e.getMessage());
        }
    }

    /**
     * Validate user
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @throws SupplyException if any error occurs
     * @return the user id
     */
    private long validateUser(String handle, AuthUser authUser, List<MemberProfileTrait> memberProfileTraits) throws SupplyException {
        if (!authUser.getHandle().equals(handle)) {
            if (!authUser.getRoles().contains(MemberProfileManager.ADMINISTRATOR)) {
                throw new SupplyException("The login user must be administrator", HttpServletResponse.SC_FORBIDDEN);
            }
            MemberProfile profile = this.memberProfileDAO.getMemberProfile(handle);
            if (profile == null) {
                throw new SupplyException("The user handle does not exists", HttpServletResponse.SC_NOT_FOUND);
            }
            if (!profile.getStatus().equals(MemberStatus.ACTIVE.toString())) {
                throw new SupplyException(String.format(Messages.HANDLE_NOT_ACTIVE, handle), HttpServletResponse.SC_FORBIDDEN);
            }

            return profile.getUserId().longValue();
        }

        if (memberProfileTraits != null && memberProfileTraits.size() > 0) {
            for (MemberProfileTrait trait : memberProfileTraits) {
                if (MemberProfileTraitValidator.BASIC_INFO_TRAIT_TYPE.equals(trait.getTraitId())) {
                    if (trait.getTraits().getData() == null || trait.getTraits().getData().size() != 1) {
                        throw new SupplyException("There should be one item trait for the basic info", HttpServletResponse.SC_BAD_REQUEST);
                    }
                    MemberProfileBasicInfo item = (MemberProfileBasicInfo) trait.getTraits().getData().get(0);
                    // item.getHanlde() should only allow letters, numbers and these: _-.{}[] if it equals to handle
                    if (!handle.equals(item.getHandle())) {
                        throw new SupplyException("The user handle does not match", HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            }
        } 
        return Long.parseLong(authUser.getUserId().getId());
    }
}
