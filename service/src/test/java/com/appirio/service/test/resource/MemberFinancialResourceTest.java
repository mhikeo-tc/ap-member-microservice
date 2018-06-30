package com.appirio.service.test.resource;

import com.appirio.service.member.api.MemberFinancial;
import com.appirio.service.member.manager.MemberFinancialManager;
import com.appirio.service.member.resources.MemberFinancialResource;
import com.appirio.service.test.BaseTest;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/25/15.
 */
public class MemberFinancialResourceTest extends BaseTest {

    private static final MemberFinancialManager memberFinancialManager = PowerMockito.mock(MemberFinancialManager.class);

    private static final Long userId = 151743L;

    private static final String handle = "Ghostar";

    private static final MemberFinancialResource memberFinancialResource = new MemberFinancialResource(memberFinancialManager);

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
        when(memberFinancialManager.getMemberFinancial(anyString(), anyObject())).
                thenReturn(memberFinancials);
    }

    @Test
    public void testGetMemberFinancial() throws SupplyException {

        ApiResponse apiResponse = memberFinancialResource.getMemberFinancial(createUser(userId.toString()), handle, null);

        verify(memberFinancialManager).getMemberFinancial(anyString(), anyObject());
        List<MemberFinancial> memberFinancials = (List<MemberFinancial>) apiResponse.getResult().getContent();

        assertNotNull(memberFinancials);
        assertEquals(memberFinancials.get(0).getAmount(), Double.valueOf(1000));
        assertEquals(memberFinancials.get(0).getStatus(), "PAID");
    }
}