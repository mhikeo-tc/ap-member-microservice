package com.appirio.service.member.dao;

import com.appirio.service.member.api.MemberFinancial;
import com.appirio.supply.dataaccess.ApiUser;
import com.appirio.supply.dataaccess.DatasourceName;
import com.appirio.supply.dataaccess.SqlQueryFile;
import com.appirio.supply.dataaccess.api.audit.AuditActionPerformer;
import com.appirio.tech.core.auth.AuthUser;

import java.util.List;

/**
 * Represents DAO for Member financial
 *
 * Created by rakeshrecharla on 7/8/15.
 */
@DatasourceName("oltp")
public interface MemberFinancialDAO {

    /**
     * Get member financial
     * @param authUser              Authentication user
     * @return MemberFinancial      Member financial
     */
    @SqlQueryFile("sql/member/member-financial-query.sql")
    List<MemberFinancial> getMemberFinancialByUser(@ApiUser @AuditActionPerformer AuthUser authUser);
}