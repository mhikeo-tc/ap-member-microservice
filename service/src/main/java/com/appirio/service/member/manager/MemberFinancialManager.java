package com.appirio.service.member.manager;

import com.appirio.service.member.api.MemberFinancial;
import com.appirio.service.member.dao.MemberFinancialDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.PaymentStatus;
import com.appirio.tech.core.auth.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Manager for Member financial
 *
 * Created by rakeshrecharla on 7/8/15.
 */
public class MemberFinancialManager {
    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberFinancialManager.class);

    /**
     * DAO for Member financial
     */
    private MemberFinancialDAO memberFinancialDAO;

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * Constructor for Member financial manager
     * @param memberFinancialDAO    Member financial dao
     * @param memberProfileDAO      Member profile dao
     */
    public MemberFinancialManager(MemberFinancialDAO memberFinancialDAO, MemberProfileDAO memberProfileDAO) {
        this.memberFinancialDAO = memberFinancialDAO;
        this.memberProfileDAO = memberProfileDAO;
    }

    /**
     * Get member financial
     * @param authUser              Authentication user
     * @return MemberFinancial      Member financial
     * @throws SupplyException      Exception for supply
     */
    public List<MemberFinancial> getMemberFinancial(String handle, AuthUser authUser) throws SupplyException {

        memberProfileDAO.validateHandle(handle, authUser, false);

        List<MemberFinancial> memberFinancials = memberFinancialDAO.getMemberFinancialByUser(authUser);
        for(int i=0; i < memberFinancials.size(); i++) {
            MemberFinancial memberFinancial = memberFinancials.get(i);
            String enumStatus = PaymentStatus.getEnumFromDescription(memberFinancial.getStatus()).toString();
            memberFinancial.setStatus(enumStatus);
        }

        return memberFinancials;
    }
}