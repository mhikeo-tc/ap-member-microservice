package com.appirio.service.member.util;

import com.appirio.service.member.api.ExternalAccountsBehance;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents Behance thread runnable
 *
 * Created by rakeshrecharla on 9/11/15.
 */
public class BehanceRunnable implements Runnable {

    /**
     * DAO for Member external accounts
     */
    private MemberExternalAccountsDAO memberExternalAccountsDAO;

    /**
     * Id of the user
     */
    private Integer userId;

    /**
     * External accounts behance
     */
    @Getter
    @Setter
    private ExternalAccountsBehance externalAccountsBehance;

    /**
     * Constructor to initialize member external accounts DAO and user id
     * @param memberExternalAccountsDAO     Member external accounts DAO
     * @param userId                        Id of the user
     */
    public BehanceRunnable(MemberExternalAccountsDAO memberExternalAccountsDAO, Integer userId) {
        this.memberExternalAccountsDAO = memberExternalAccountsDAO;
        this.userId = userId;
    }

    /**
     * thread run method
     */
    public void run() {
        externalAccountsBehance = memberExternalAccountsDAO.getMemberExternalAccountsBehance(userId);
    }
}