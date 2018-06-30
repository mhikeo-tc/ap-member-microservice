package com.appirio.service.member.util;

import com.appirio.service.member.api.ExternalAccountsTwitter;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents Twitter thread runnable
 *
 * Created by rakeshrecharla on 9/11/15.
 */
public class TwitterRunnable implements Runnable {

    /**
     * DAO for Member external accounts
     */
    private MemberExternalAccountsDAO memberExternalAccountsDAO;

    /**
     * Id of the user
     */
    private Integer userId;

    /**
     * External accounts twitter
     */
    @Getter
    @Setter
    private ExternalAccountsTwitter externalAccountsTwitter;

    /**
     * Constructor to initialize member external accounts DAO and userId
     * @param memberExternalAccountsDAO     Member external accounts DAO
     * @param userId                        Id of the user
     */
    public TwitterRunnable(MemberExternalAccountsDAO memberExternalAccountsDAO, Integer userId) {
        this.memberExternalAccountsDAO = memberExternalAccountsDAO;
        this.userId = userId;
    }

    /**
     * Thread run method
     */
    public void run() {
        externalAccountsTwitter = memberExternalAccountsDAO.getMemberExternalAccountsTwitter(userId);
    }
}