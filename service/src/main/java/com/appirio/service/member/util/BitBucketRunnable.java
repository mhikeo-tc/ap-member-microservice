package com.appirio.service.member.util;

import com.appirio.service.member.api.ExternalAccountsBitBucket;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents Bit bucket thread runnable
 *
 * Created by rakeshrecharla on 9/11/15.
 */
public class BitBucketRunnable implements Runnable {

    /**
     * DAO for Member external accounts
     */
    private MemberExternalAccountsDAO memberExternalAccountsDAO;

    /**
     * Id of the user
     */
    private Integer userId;

    /**
     * External accounts bit bucket
     */
    @Getter
    @Setter
    private ExternalAccountsBitBucket externalAccountsBitBucket;

    /**
     * Constructor to initialize member external accounts DAO and userId
     * @param memberExternalAccountsDAO     Member external accounts DAO
     * @param userId                        Id of the user
     */
    public BitBucketRunnable(MemberExternalAccountsDAO memberExternalAccountsDAO, Integer userId) {
        this.memberExternalAccountsDAO = memberExternalAccountsDAO;
        this.userId = userId;
    }

    /**
     * Thread run method
     */
    public void run() {
        externalAccountsBitBucket = memberExternalAccountsDAO.getMemberExternalAccountsBitBucket(userId);
    }
}