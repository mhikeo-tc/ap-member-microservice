package com.appirio.service.test.manager;

import com.appirio.service.member.api.MemberFinancial;
import com.appirio.service.member.dao.MemberFinancialDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberFinancialManager;
import com.appirio.service.test.BaseTest;
import com.appirio.supply.SupplyException;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/25/15.
 */
public class MemberFinancialManagerTest extends BaseTest {

    private static final MemberFinancialDAO memberFinancialDAO = PowerMockito.mock(MemberFinancialDAO.class);

    private static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    private static final MemberFinancialManager memberFinancialManager = new MemberFinancialManager(
            memberFinancialDAO, memberProfileDAO);

    private static final Long userId = 151743L;

    private static final String handle = "Ghostar";

    private List<MemberFinancial> buildMemberFinancial() {

        MemberFinancial memberFinancial = new MemberFinancial();
        memberFinancial.setUserId(userId);
        memberFinancial.setAmount(1000D);
        memberFinancial.setStatus("PAID");

        List<MemberFinancial> memberFinancials = new ArrayList<MemberFinancial>();
        memberFinancials.add(memberFinancial);
        return memberFinancials;
    }

    @Before
    public void init() {

        List<MemberFinancial> memberFinancials = buildMemberFinancial();
        when(memberFinancialDAO.getMemberFinancialByUser(anyObject())).thenReturn(memberFinancials);
    }

    @Test
    public void testGetMemberFinancial() throws SupplyException {

        List<MemberFinancial> memberFinancials = memberFinancialManager.getMemberFinancial(handle, createUser(userId.toString()));
        verify(memberFinancialDAO).getMemberFinancialByUser(anyObject());

        assertNotNull(memberFinancials);
        assertEquals(memberFinancials.get(0).getAmount(), Double.valueOf(1000));
        assertEquals(memberFinancials.get(0).getStatus(), "PAID");
    }
}