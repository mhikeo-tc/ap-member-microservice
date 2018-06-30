
package com.appirio.service.member.manager;

import com.appirio.service.member.api.MemberHistoryStats;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberHistoryStatsDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.supply.SupplyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for Member stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class MemberHistoryStatsManager {
    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberHistoryStatsManager.class);

    /**
     * DAO for Member history stats
     */
    private MemberHistoryStatsDAO memberHistoryStatsDAO;

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * Constructor to initialize DAOs for member stats and member copilot stats
     * @param memberHistoryStatsDAO      Member history stats DAO
     * @param memberProfileDAO           Member profile DAO
     */
    public MemberHistoryStatsManager(MemberHistoryStatsDAO memberHistoryStatsDAO,
                                     MemberProfileDAO memberProfileDAO) {
        this.memberHistoryStatsDAO = memberHistoryStatsDAO;
        this.memberProfileDAO = memberProfileDAO;
    }

    /**
     * Get development, design, data science and copilot statistics
     * @param handle                Handle of the user
     * @return MemberHistoryStats   Member history statistics
     * @throws SupplyException      Exception for supply
     */
    public MemberHistoryStats getMemberHistoryStats(String handle) throws SupplyException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, null, true);
        MemberHistoryStats memberHistoryStats = memberHistoryStatsDAO.getMemberHistoryStats(
                memberProfile.getUserId());
        // Handle is set to be same as member profile handle in order
        // to accommodate handle modification through admin tool
        if (memberHistoryStats != null) {
            memberHistoryStats.setHandle(memberProfile.getHandle());
            memberHistoryStats.setHandleLower(memberProfile.getHandleLower());
        }
        return memberHistoryStats;
    }
}