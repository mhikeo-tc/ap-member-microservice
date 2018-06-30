
package com.appirio.service.member.manager;

import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.MemberStats;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberStatsDAO;
import com.appirio.supply.SupplyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for Member stats
 *
 * Created by rakeshrecharla on 7/8/15.
 */
public class MemberStatsManager {
    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberStatsManager.class);

    /**
     * DAO for Member stats
     */
    private MemberStatsDAO memberStatsDAO;

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * Constructor to initialize DAOs for member stats and member copilot stats
     * @param memberStatsDAO        Member stats DAO
     */
    public MemberStatsManager(MemberStatsDAO memberStatsDAO, MemberProfileDAO memberProfileDAO) {
        this.memberStatsDAO = memberStatsDAO;
        this.memberProfileDAO = memberProfileDAO;
    }

    /**
     * Get development, design, data science and copilot statistics
     * @param handle         Handle of the user
     * @return MemberStats   Member statistics
     */
    public MemberStats getMemberStats(String handle) throws SupplyException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, null, true);
        MemberStats memberStats = memberStatsDAO.getMemberStats(memberProfile.getUserId());
        // Handle is set to be same as member profile handle in order
        // to accommodate handle modification through admin tool
        if (memberStats != null) {
            memberStats.setHandle(memberProfile.getHandle());
            memberStats.setHandleLower(memberProfile.getHandleLower());
        }
        return memberStats;
    }
}