package com.appirio.service.member.manager;

import com.appirio.service.member.api.ExternalLinkURL;
import com.appirio.service.member.api.MemberExternalLink;
import com.appirio.service.member.api.MemberExternalLinkData;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberExternalLinksDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.supply.Messages;
import com.appirio.supply.SupplyException;
import com.appirio.supply.ValidationException;
import com.appirio.tech.core.auth.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Manager for Member external links
 *
 * Created by rakeshrecharla on 10/19/15.
 */
public class MemberExternalLinksManager {
    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalLinksManager.class);

    /**
     * DAO for Member external links
     */
    private MemberExternalLinksDAO memberExternalLinksDAO;

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * Constructor to initialize DAOs for member external accounts and member profile
     * @param memberExternalLinksDAO    Member external accounts DAO
     * @param memberProfileDAO          Member profile DAO
     */
    public MemberExternalLinksManager(MemberExternalLinksDAO memberExternalLinksDAO,
                                      MemberProfileDAO memberProfileDAO) {
        this.memberExternalLinksDAO = memberExternalLinksDAO;
        this.memberProfileDAO = memberProfileDAO;
    }

    /**
     * Get MD5 hash
     * @param txt                          String text
     * @return                             Hash
     * @throws NoSuchAlgorithmException    Exception for no such algorithm
     */
    public static String getHash(String txt) throws NoSuchAlgorithmException {

        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(txt.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }

    /**
     * Get member external links
     * @param handle        Handle of the user
     * @return              MemberExternalLinkData list
     */
    public List<MemberExternalLinkData> getMemberExternalLinks(String handle) throws SupplyException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, null, true);
        Map<String, MemberExternalLinkData> linksDataMap = new HashMap<String, MemberExternalLinkData>();

        List<MemberExternalLink> memberExternalLinkList = memberExternalLinksDAO.getMemberExternalLinks(
                memberProfile.getUserId());
        List<MemberExternalLinkData> memberExternalLinkDataList = memberExternalLinksDAO.getMemberExternalLinksData(
                memberProfile.getUserId());
        logger.debug("Total external links : " + memberExternalLinkList.size());
        logger.debug("Total external links data : " + memberExternalLinkDataList.size());

        for (MemberExternalLinkData memberExternalLinkData : memberExternalLinkDataList) {
            linksDataMap.put(memberExternalLinkData.getKey(), memberExternalLinkData);
        }

        List<MemberExternalLinkData> memberExternalLinkNewDataList = new ArrayList<MemberExternalLinkData>();
        for (int linksIndex = 0; linksIndex < memberExternalLinkList.size(); linksIndex++) {
            MemberExternalLink memberExternalLink = memberExternalLinkList.get(linksIndex);
            if (!memberExternalLink.getIsDeleted()) {
                MemberExternalLinkData memberExternalLinkData;
                String key = memberExternalLink.getKey();
                if (linksDataMap.containsKey(key)) {
                    memberExternalLinkData = linksDataMap.get(key);
                    memberExternalLinkData.setHandle(memberProfile.getHandle());
                } else {
                    memberExternalLinkData = new MemberExternalLinkData();
                    memberExternalLinkData.setUserId(memberProfile.getUserId());
                    memberExternalLinkData.setKey(key);
                    memberExternalLinkData.setHandle(memberProfile.getHandle());
                }
                memberExternalLinkData.setUrl(memberExternalLink.getUrl());
                memberExternalLinkData.setSynchronizedAt(memberExternalLink.getSynchronizedAt());
                memberExternalLinkNewDataList.add(memberExternalLinkData);
            }
        }
        return memberExternalLinkNewDataList;
    }

    /**
     * Validate external link URL
     * @param externalLinkURL               External link URL
     * @throws IllegalAccessException       Exception for illegal access
     * @throws InvocationTargetException    Exception for invocation target
     * @throws InstantiationException       Exception for instantiation
     * @throws SupplyException              Exception for supply
     * @throws NoSuchMethodException        Exception for no such method
     */
    private void validateExternalLinkUrl(ExternalLinkURL externalLinkURL) throws IllegalAccessException,
            InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {
        List<String> validationMessages = externalLinkURL.validate();

        if (validationMessages != null && validationMessages.size() > 0) {
            String concatMessage = "";
            for (String msg : validationMessages) {
                concatMessage += msg + " | ";
            }

            ValidationException validationException = new ValidationException(concatMessage);
            validationException.setValidationMessages(validationMessages);
            throw validationException;
        }
    }

    /**
     * Create member external link
     * @param handle                      Handle of the user
     * @param authUser                    Authentication user
     * @param externalLinkURL             External link URL
     * @return                            Member External link data list
     * @throws SupplyException            Exception for Supply
     * @throws NoSuchAlgorithmException   Exception for no such algorithm
     */
    public MemberExternalLink createMemberExternalLink(String handle, AuthUser authUser, ExternalLinkURL externalLinkURL)
            throws SupplyException, NoSuchAlgorithmException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, authUser, false);
        validateExternalLinkUrl(externalLinkURL);

        MemberExternalLink memberExternalLink = new MemberExternalLink();
        memberExternalLink.setUserId(memberProfile.getUserId());
        memberExternalLink.setUrl(externalLinkURL.getUrl());

        if (externalLinkURL.getUrl() != null) {
            String url = memberExternalLink.getUrl();
            String key = memberProfile.getUserId() + ":" + url.replaceFirst("http://", "").replaceFirst("https://", "");
            logger.debug("key : " + key);
            memberExternalLink.setKey(getHash(key));
        }

        memberExternalLink.setSynchronizedAt(0L);
        memberExternalLink.setIsDeleted(false);
        memberExternalLink.setHasErrored(false);
        memberExternalLink.setCreatedAt(new Date());
        memberExternalLink.setUpdatedAt(new Date());

        MemberExternalLink existingMemberExternalLink = memberExternalLinksDAO.getMemberExternalLink(
                memberProfile.getUserId(), memberExternalLink.getKey());
        if (existingMemberExternalLink != null && existingMemberExternalLink.getIsDeleted() == false) {
            throw new  SupplyException(String.format(Messages.EXTERNAL_LINK_URL_ALREADY_EXISTS, memberExternalLink.getUrl(),
                    memberProfile.getHandle()), HttpServletResponse.SC_BAD_REQUEST);
        }

        memberExternalLinksDAO.updateMemberExternalLink(memberExternalLink);
        return memberExternalLinksDAO.getMemberExternalLink(memberProfile.getUserId(), memberExternalLink.getKey());
    }

    /**
     * Delete member external link
     * @param handle                Handle of the user
     * @param key                   Key
     * @param authUser              Authentication user
     * @return                      MemberExternalLinkData list
     * @throws SupplyException      Exception for supply
     */
    public MemberExternalLink deleteMemberExternalLink(String handle, String key, AuthUser authUser)
            throws SupplyException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, authUser, false);
        MemberExternalLink memberExternalLink = memberExternalLinksDAO.getMemberExternalLink(memberProfile.getUserId(), key);
        if (memberExternalLink == null || memberExternalLink.getIsDeleted() == true) {
            throw new SupplyException(String.format(Messages.EXTERNAL_LINK_KEY_NOT_FOUND, key, memberProfile.getHandle()),
                    HttpServletResponse.SC_NOT_FOUND);
        }

        memberExternalLink.setIsDeleted(true);
        memberExternalLinksDAO.updateMemberExternalLink(memberExternalLink);
        return memberExternalLinksDAO.getMemberExternalLink(memberProfile.getUserId(), key);
    }
}