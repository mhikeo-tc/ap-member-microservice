package com.appirio.service.test.dao;

import com.appirio.service.member.api.MemberFinancial;
import com.appirio.service.member.dao.MemberFinancialDAO;
import com.appirio.supply.SupplyException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.skife.jdbi.v2.Query;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rakeshrecharla on 8/25/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Query.class)
public class MemberFinancialDAOTest extends GenericDAOTest {

    private MemberFinancialDAO memberFinancialDAO;

    private static final Long userId = 151743L;

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
    public void init() throws SupplyException {

        List<MemberFinancial> memberFinancials = buildMemberFinancial();

        memberFinancialDAO = createDAO(memberFinancials, null, MemberFinancialDAO.class);
    }

    //@Test
    public void testGetMemberFinancial() {

        // Invoke method
        List<MemberFinancial> memberFinancials = memberFinancialDAO.getMemberFinancialByUser(createUser(userId.toString()));

        assertNotNull(memberFinancials);

        // Verify that JDBI was called
        verifyListObjectQuery(mocker);

        assertEquals(userId.toString(), params.get("loggedInUserId"));
        assertEquals(memberFinancials.get(0).getAmount(), Double.valueOf(1000));
        assertEquals(memberFinancials.get(0).getStatus(), "PAID");
    }

}
