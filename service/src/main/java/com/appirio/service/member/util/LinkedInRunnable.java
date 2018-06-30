package com.appirio.service.member.util;

import com.appirio.service.member.api.ExternalAccountsLinkedIn;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents LinkedIn thread runnable
 *
 * Created by rakeshrecharla on 9/11/15.
 */
public class LinkedInRunnable implements Runnable {

    /**
     * DAO for Member external accounts
     */
    private MemberExternalAccountsDAO memberExternalAccountsDAO;

    /**
     * Id of the user
     */
    private Integer userId;

    /**
     * External accounts linkedin
     */
    @Getter
    @Setter
    private ExternalAccountsLinkedIn externalAccountsLinkedIn;

    /**
     * Constructor to initialize member external accounts DAO and userId
     * @param memberExternalAccountsDAO     Member external accounts DAO
     * @param userId                        Id of the user
     */
    public LinkedInRunnable(MemberExternalAccountsDAO memberExternalAccountsDAO, Integer userId) {
        this.memberExternalAccountsDAO = memberExternalAccountsDAO;
        this.userId = userId;
    }

    /**
     * Thread run method
     */
    public void run() {
        externalAccountsLinkedIn = memberExternalAccountsDAO.getMemberExternalAccountsLinkedIn(userId);
    }
}