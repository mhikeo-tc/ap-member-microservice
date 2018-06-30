package com.appirio.service.member.manager;

import com.appirio.service.member.api.ExternalAccounts;
import com.appirio.service.member.api.MemberExternalAccounts;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.util.*;
import com.appirio.supply.SupplyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager for Member external accounts
 *
 * Created by rakeshrecharla on 9/10/15.
 */
public class MemberExternalAccountsManager {
    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalAccountsManager.class);

    /**
     * DAO for Member external accounts
     */
    private MemberExternalAccountsDAO memberExternalAccountsDAO;

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * Constructor to initialize DAOs for member external accounts and member profile
     * @param memberExternalAccountsDAO     Member external accounts DAO
     * @param memberProfileDAO              Member profile DAO
     */
    public MemberExternalAccountsManager(MemberExternalAccountsDAO memberExternalAccountsDAO,
                                         MemberProfileDAO memberProfileDAO) {
        this.memberExternalAccountsDAO = memberExternalAccountsDAO;
        this.memberProfileDAO = memberProfileDAO;
    }

    /**
     * Get Member external accounts
     * @param handle            Handle of the user
     * @return MemberStats      Member statistics
     */
    public MemberExternalAccounts getMemberExternalAccounts(String handle) throws SupplyException, InterruptedException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, null, true);

        MemberExternalAccounts memberExternalAccounts = new MemberExternalAccounts();
        memberExternalAccounts.setUserId(memberProfile.getUserId());
        memberExternalAccounts.setHandle(memberProfile.getHandle());
        ThreadRunnableFactory threadRunnableFactory = new ThreadRunnableFactory(memberExternalAccountsDAO, memberProfile.getUserId());

        List<ExternalAccounts> externalAccountsList = memberExternalAccountsDAO.getExternalAccounts(memberProfile.getUserId().toString());
        logger.debug("Total external accounts : " + externalAccountsList.size());
        for (ExternalAccounts externalAccounts : externalAccountsList) {
            logger.debug("Account type : " + externalAccounts.getAccountType());
            if (externalAccounts.getIsDeleted() != null && externalAccounts.getIsDeleted() == false) {
                 threadRunnableFactory.startThread(externalAccounts.getAccountType());
            }
        }

        for (ExternalAccounts externalAccounts : externalAccountsList) {
            if (externalAccounts.getIsDeleted() != null && externalAccounts.getIsDeleted() == false) {
                threadRunnableFactory.joinThread(externalAccounts.getAccountType());
            }
        }

        List<Throwable> exceptions = threadRunnableFactory.getExceptions();
        if (exceptions != null && exceptions.size() > 0) {
            List<String> messages = new ArrayList<String>();
            for (Throwable throwable : exceptions) {
                messages.add(throwable.getMessage());
            }

            throw new SupplyException(messages.toString(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        memberExternalAccounts.setBehance(((BehanceRunnable)threadRunnableFactory.getThreadRunnable()
                .get("behance")).getExternalAccountsBehance());
        memberExternalAccounts.setBitbucket(((BitBucketRunnable)threadRunnableFactory.getThreadRunnable()
                .get("bitbucket")).getExternalAccountsBitBucket());
        memberExternalAccounts.setDribbble(((DribbleRunnable) threadRunnableFactory.getThreadRunnable()
                .get("dribbble")).getExternalAccountsDribbble());
        memberExternalAccounts.setGithub(((GithubRunnable)threadRunnableFactory.getThreadRunnable()
                .get("github")).getExternalAccountsGithub());
        memberExternalAccounts.setLinkedin(((LinkedInRunnable)threadRunnableFactory.getThreadRunnable()
                .get("linkedin")).getExternalAccountsLinkedIn());
        memberExternalAccounts.setStackoverflow(((StackOverflowRunnable)threadRunnableFactory.getThreadRunnable()
                .get("stackoverflow")).getExternalAccountsStackOverflow());
        memberExternalAccounts.setTwitter(((TwitterRunnable)threadRunnableFactory.getThreadRunnable()
                .get("twitter")).getExternalAccountsTwitter());

        return memberExternalAccounts;
    }
}