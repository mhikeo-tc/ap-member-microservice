package com.appirio.service.member.manager;

import com.amazonaws.util.json.JSONObject;
import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.exception.EmptyEventException;
import com.appirio.eventsbus.api.client.exception.EncodingEventException;
import com.appirio.service.EmailVerificationConfiguration;
import com.appirio.service.member.api.*;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberStatsDAO;
import com.appirio.service.member.eventbus.EventBusServiceClient;
import com.appirio.service.member.eventbus.EventMessage;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import com.appirio.supply.dataaccess.FileInvocationHandler;
import com.appirio.supply.dataaccess.queryhandler.handler.ValidationHandler;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import com.appirio.tech.core.auth.AuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


/**
 * Represents the MemberProfileManager used to manage the user profile data
 *
 * <p>
 * Changes in the version 1.1 (Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0)
 * - modify updateMemeberProfile to send the email verificaiton
 * - add the verifyUserEmail method
 * - modify getMemberProfile so that it does not return the new email information for the anonymous user
 * </p>
 *
 * @author TCCoder
 * @version 1.1
 *
 */
public class MemberProfileManager {

    /**
     * The role name for admin
     */
    public static final String ADMINISTRATOR = "administrator";

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberProfileManager.class);

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * DAO for member stats
     */
    private MemberStatsDAO memberStatsDAO;

    /**
     * Photo URL domain
     */
    private String photoURLDomain;

    /**
     * File Invocation handler
     */
    private FileInvocationHandler fileInvocationHandler;

    /**
     * Kafka Event Producer
     */
    private EventProducer eventProducer;

    /**
     * The event bus service client field used to send the event
     */
    private final EventBusServiceClient eventBusServiceClient;

    /**
     * The email verification config field
     */
    private final EmailVerificationConfiguration emailVerificationConfig;

    /**
     * Constructor to initialize member profile DAO and file service domain
     *
     * @param memberProfileDAO the memberProfileDAO to use
     * @param memberStatsDAO the memberStatsDAO to use
     * @param photoURLDomain the photoURLDomain to use
     * @param fileInvocationHandler the fileInvocationHandler to use
     * @param eventProducer the eventProducer to use
     * @param eventBusServiceClient the eventBusServiceClient to use
     */
    public MemberProfileManager(MemberProfileDAO memberProfileDAO, MemberStatsDAO memberStatsDAO,
                                String photoURLDomain, FileInvocationHandler fileInvocationHandler,
                                EventProducer eventProducer, EventBusServiceClient eventBusServiceClient,
                                EmailVerificationConfiguration emailVerificationConfig) {
        this.memberProfileDAO = memberProfileDAO;
        this.memberStatsDAO = memberStatsDAO;
        this.photoURLDomain = photoURLDomain;
        this.fileInvocationHandler = fileInvocationHandler;
        this.eventProducer = eventProducer;
        this.eventBusServiceClient = eventBusServiceClient;
        this.emailVerificationConfig = emailVerificationConfig;
    }

    /**
     * Get Member profile
     * @param handle            Handle of the user
     * @param authUser          Authentication user
     * @return MemberProfile    Member profile
     * @throws SupplyException  Exception for supply
     */
    public MemberProfile getMemberProfile(String handle, AuthUser authUser) throws SupplyException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, authUser, true);

        MemberStats memberStats = memberStatsDAO.getMemberStats(memberProfile.getUserId());

        if(memberStats != null && memberStats.getMaxRating() != null) {
        	memberProfile.setMaxRating(memberStats.getMaxRating());
        }
        // if user is not logged in
        // if user is not admin user && not asking for my own profile
        if (authUser == null || !(isAdmin(authUser) || memberProfile.getUserId().equals(
                Integer.valueOf(authUser.getUserId().toString())))) {
            memberProfile.setAddresses(null);
            memberProfile.setEmail(null);
            memberProfile.setFirstName(null);
            memberProfile.setLastName(null);
            memberProfile.setOtherLangName(null);
            memberProfile.setNewEmail(null);
            memberProfile.setEmailVerifyToken(null);
            memberProfile.setEmailVerifyTokenDate(null);
        }

        return memberProfile;
    }

    /**
     * Update member profile
     *
     * @param handle Handle of the user
     * @param authUser Authentication user
     * @param memberProfile Member profile
     * @return MemberProfile Member profile
     * @throws SupplyException if any error occurs
     * @throws IllegalAccessException if any error occurs
     * @throws InvocationTargetException if any error occurs
     * @throws InstantiationException if any error occurs
     * @throws NoSuchMethodException if any error occurs
     */
    public MemberProfile updateMemberProfile(String handle, AuthUser authUser, MemberProfile memberProfile) throws
            IllegalAccessException, NoSuchMethodException, InstantiationException, SupplyException,
            InvocationTargetException, JsonProcessingException {

        MemberProfile existingMemberProfile = memberProfileDAO.validateHandle(handle, authUser, false);

        boolean verifyEmail = false;
        if (!existingMemberProfile.getEmail().equals(memberProfile.getEmail())) {
            verifyEmail = true;
            if (!EmailValidator.getInstance().isValid((String) memberProfile.getEmail())) {
                throw new SupplyException("The email is invalid:" + memberProfile.getEmail(), HttpServletResponse.SC_BAD_REQUEST);
            } else {
                String token = RandomStringUtils.randomAlphanumeric(16);
                memberProfile.setNewEmail((String) memberProfile.getEmail());
                memberProfile.setEmail((String) existingMemberProfile.getEmail());
                memberProfile.setEmailVerifyToken(token);
                memberProfile.setEmailVerifyTokenDate(new Date());
            }
        }

        memberProfile.setUserId(Integer.valueOf(authUser.getUserId().toString()));
        memberProfile.setCreatedBy(existingMemberProfile.getCreatedBy());
        memberProfile.setCreatedAt(existingMemberProfile.getCreatedAt());
        memberProfile.setUpdatedBy(authUser.getUserId().toString());
        memberProfile.setUpdatedAt(new Date());
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());
        memberProfile.setHandleLower(memberProfile.getHandle().toLowerCase());

        memberProfileDAO.updateMemberProfile(memberProfile, true);

        // fire event on to the kafka bus to update member profile
        ObjectMapper mapper = new ObjectMapper();
        publishKafkaEvent(mapper.valueToTree(memberProfile), "member-profile-update-event");
        if (memberProfile.getPhotoURL() == null) {
            // Delete image from informix
            // Create MemberProfilePhotoUpdateEvent object to publish to Kafka bus for informix update
            MemberProfilePhotoUpdateEvent eventObj = new
                    MemberProfilePhotoUpdateEvent(memberProfile.getUserId(), memberProfile.getPhotoURL());
            publishKafkaEvent(mapper.valueToTree(eventObj), "member-profile-photo-update-event");
        }

        if (verifyEmail) {
            this.fireEmailVerificationEvent(authUser, memberProfile);
        }

        MemberProfile result = memberProfileDAO.getMemberProfile(handle);
        result.setEmailVerifyToken(null);
        result.setEmailVerifyTokenDate(null);

        return result;
    }

    /**
     * Verify user email
     *
     * @param handle the handle to use
     * @param authUser the authUser to use
     * @param newEmail the newEmail to use
     * @param oldEmail the oldEmail to use
     * @param token the token to use
     * @throws SupplyException if any error occurs
     * @throws IllegalAccessException if any error occurs
     * @throws InvocationTargetException if any error occurs
     * @throws InstantiationException if any error occurs
     * @throws NoSuchMethodException if any error occurs
     */
    public void verifyUserEmail(String handle, AuthUser authUser, String newEmail, String oldEmail, String token) throws SupplyException,
        IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException{
        MemberProfile profile = memberProfileDAO.validateHandle(handle, authUser, false);

        if (!handle.equals(profile.getHandle())) {
            throw new SupplyException("Please login by yourself", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (profile.getNewEmail() == null || profile.getEmailVerifyToken() == null || profile.getEmailVerifyTokenDate() == null) {
            throw new SupplyException("There is no email to verify", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (!profile.getEmail().equals(oldEmail)) {
            throw new SupplyException("The old email does not match:" + oldEmail, HttpServletResponse.SC_BAD_REQUEST);
        }
        if (!profile.getNewEmail().equals(newEmail)) {
            throw new SupplyException("The new email does not match:" + newEmail, HttpServletResponse.SC_BAD_REQUEST);
        }
        if (!profile.getEmailVerifyToken().equals(token)) {
            throw new SupplyException("The token does not match:" + token, HttpServletResponse.SC_BAD_REQUEST);
        }

        profile.setNewEmail(null);
        profile.setEmailVerifyToken(null);

        if (new Date().getTime() - profile.getEmailVerifyTokenDate().getTime() >= 1000 * 60 * this.emailVerificationConfig.getTokenExpireTimeInMinutes()) {
            profile.setEmailVerifyTokenDate(null);
            memberProfileDAO.updateMemberProfile(profile, true);
            throw new SupplyException("The token is expired, please verify the email again", HttpServletResponse.SC_BAD_REQUEST);
        }

        // the token is valid
        profile.setEmail(newEmail);
        profile.setEmailVerifyTokenDate(null);

        memberProfileDAO.updateMemberProfile(profile, true);
    }

    /**
     * Fire email verification event
     *
     * @param authUser the authUser to use
     * @param verificationToken the verificationToken to use
     */
    private void fireEmailVerificationEvent(AuthUser authUser, MemberProfile profile) {
        EventMessage msg = new EventMessage();
        msg.setTopic("member.action.email.profile.emailchange.verification").setMimeType("application/json").setOriginator("tc-member-profile").setTimestamp(new Date());
        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("subject", this.emailVerificationConfig.getSubject());
        data.put("userHandle", authUser.getHandle()); 
        data.put("verificationAgreeUrl", this.emailVerificationConfig.getVerificationAgreeUrl());
        data.put("verificationToken", profile.getEmailVerifyToken());

        List<String> recipients = new ArrayList<String>();
        recipients.add(profile.getNewEmail());

        msg.setPayload("data", data);
        msg.setPayload("recipients", recipients);

        this.eventBusServiceClient.fireEvent(msg);
    }

    /**
     * Validate and return content type
     * @param photoContentType photo content type
     * @return String          type
     */
    private String validateContentType(PhotoContentType photoContentType) throws IllegalAccessException,
            InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {

        List<String> validationMessages = photoContentType.validate();
        ValidationHandler.validationException(validationMessages);

        return photoContentType.getContentType().split("/")[1];
    }

    /**
     * Validate and return content type
     * @param photoTokenContentType photo token
     * @return String    content type
     */
    private String validateToken(PhotoTokenContentType photoTokenContentType) throws SupplyException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<String> validationMessages = photoTokenContentType.validate();
        ValidationHandler.validationException(validationMessages);

        PhotoContentType photoContentType = new PhotoContentType();
        photoContentType.setContentType(photoTokenContentType.getContentType());
        return validateContentType(photoContentType);
    }

    /**
     * Generate photoUploadUrl
     * @param handle        Handle of the user
     * @param authUser      Authentication user
     * @return String       Photo URL
     * @throws Exception    Exception
     */
    public PhotoTokenURL generatePhotoUploadUrl(String handle, AuthUser authUser,
                                                PhotoContentType photoContentType) throws Exception {

        memberProfileDAO.validateHandle(handle, authUser, false);
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date formateDate = df.parse(df.format(date));
        long token = formateDate.getTime();

        String type = validateContentType(photoContentType);
        logger.debug("Image type : " + type);

        String fileName = handle + "-" + token + "." + type;
        String filePath = "member/profile/" + fileName;
        logger.debug("File path : " + filePath);

        ApiResponse res = fileInvocationHandler.makeRequest(authUser, filePath, "/uploadurl", "post",
                photoContentType.getContentType(), true);
        JSONObject response = new JSONObject(res);
        JSONObject result = response.getJSONObject("result");
        JSONObject content = result.getJSONObject("content");

        PhotoTokenURL photoTokenURL = new PhotoTokenURL();
        photoTokenURL.setPreSignedURL(content.getString("preSignedURL"));
        photoTokenURL.setToken(token);
        return photoTokenURL;
    }

    /**
     * Update photo URL
     * @param handle                    Handle of the user
     * @param authUser                  Authentication user
     * @param photoTokenContentType     Photo token and content type
     */
    public String updatePhoto(String handle, AuthUser authUser, PhotoTokenContentType photoTokenContentType)
            throws SupplyException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException, JsonProcessingException, UnsupportedEncodingException {

        memberProfileDAO.validateHandle(handle, authUser, false);
        MemberProfile memberProfile = memberProfileDAO.getMemberProfile(handle);
        memberProfile.setUpdatedBy(authUser.getUserId().toString());
        memberProfile.setUpdatedAt(new Date());

        String type = validateToken(photoTokenContentType);
        logger.debug("Image type : " + type);

        String fileName = handle + "-" + photoTokenContentType.getToken() + "." + type;
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        logger.debug("Encoded file name : " + encodedFileName);

        String photoURL = photoURLDomain + "/member/profile/" + encodedFileName;
        logger.debug("Photo URL : " + photoURL);

        memberProfile.setPhotoURL(photoURL);
        memberProfileDAO.updateMemberProfile(memberProfile, false);

        // Create MemberProfilePhotoUpdateEvent object to publish to Kafka bus for Informix update
        MemberProfilePhotoUpdateEvent eventObj = new
                MemberProfilePhotoUpdateEvent(memberProfile.getUserId(), photoURL);

        ObjectMapper mapper = new ObjectMapper();
        publishKafkaEvent(mapper.valueToTree(eventObj), "member-profile-photo-update-event");

        return photoURL;
    }

    /**
     * fireKafkaEvent publishes profile update events on to the kafka bus
     *
     * @param json      Json string
     * @param topic     Topic name
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
     * Check whether given role has administrator role.
     * @param user the auth user
     * @return true if user is not null and has administrator role
     */
    private static boolean isAdmin(AuthUser user){
        return user != null && user.hasRole(ADMINISTRATOR);
    }
}
