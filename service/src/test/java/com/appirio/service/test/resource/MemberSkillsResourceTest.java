package com.appirio.service.test.resource;

import com.amazonaws.util.json.JSONException;
import com.appirio.service.member.api.MemberAggregatedSkills;
import com.appirio.service.member.api.MemberEnteredSkills;
import com.appirio.service.member.api.MemberSkills;
import com.appirio.service.member.manager.MemberSkillsManager;
import com.appirio.service.member.resources.MemberSkillsResource;
import com.appirio.service.test.BaseTest;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.request.PostPutRequest;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 9/12/15.
 */
public class MemberSkillsResourceTest extends BaseTest {

    private static final MemberSkillsManager memberSkillsManager = PowerMockito.mock(MemberSkillsManager.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private static final MemberSkillsResource memberSkillsResource = new MemberSkillsResource(memberSkillsManager);

    private MemberAggregatedSkills builMemberAggregatedSkills() {

        MemberAggregatedSkills memberAggregatedSkills = new MemberAggregatedSkills();
        memberAggregatedSkills.setUserId(userId);
        memberAggregatedSkills.setUserHandle(handle);
        memberAggregatedSkills.setHandleLower(handle.toLowerCase());

        MemberSkills memberSkills = new MemberSkills();
        memberSkills.setScore(98.0);
        memberSkills.setHidden(false);
        memberSkills.setSources(new HashSet<String>(Arrays.asList("CHALLENGE")));

        MemberSkills memberSkills1 = new MemberSkills();
        memberSkills1.setScore(1.0);
        memberSkills1.setHidden(false);
        memberSkills1.setSources(new HashSet<String>(Arrays.asList("USER_ENTERED")));

        Map<Long, MemberSkills> memberSkillsMap = new HashMap<Long, MemberSkills>();
        memberSkillsMap.put(251L, memberSkills);
        memberSkillsMap.put(150L, memberSkills1);
        memberAggregatedSkills.setSkills(memberSkillsMap);

        return memberAggregatedSkills;
    }

    @Before
    public void init() throws JSONException, SupplyException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {

        MemberAggregatedSkills memberAggregatedSkills = builMemberAggregatedSkills();
        when(memberSkillsManager.getMemberAggregatedSkills(handle)).thenReturn(memberAggregatedSkills);
        when(memberSkillsManager.updateMemberSkills(anyString(), anyObject(), anyObject())).
                thenReturn(memberAggregatedSkills);
    }

    @Test
    public void testGetMemberSkills() throws JSONException, SupplyException {

        ApiResponse apiResponse = memberSkillsResource.getMemberAggregatedSkills(handle, null);

        verify(memberSkillsManager).getMemberAggregatedSkills(anyString());
        MemberAggregatedSkills memberAggregatedSkills = (MemberAggregatedSkills) apiResponse.getResult().getContent();

        assertNotNull(memberAggregatedSkills);
        assertEquals(memberAggregatedSkills.getUserId(), userId);
        assertEquals(memberAggregatedSkills.getUserHandle(), handle);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getHidden(), false);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getSources(),
                new HashSet<String>(Arrays.asList("CHALLENGE")));
    }

    @Test
    public void testUpdateMemberSkills() throws JSONException, IllegalAccessException, SupplyException,
            NoSuchMethodException, InstantiationException, InvocationTargetException {

        PostPutRequest<MemberEnteredSkills> postPutRequest = new PostPutRequest<MemberEnteredSkills>();
        ApiResponse apiResponse = memberSkillsResource.updateMemberSkills(handle, createUser(
                userId.toString()), postPutRequest);
        verify(memberSkillsManager).updateMemberSkills(anyString(), anyObject(), anyObject());

        MemberAggregatedSkills memberAggregatedSkills = (MemberAggregatedSkills) apiResponse.getResult().getContent();
        assertNotNull(memberAggregatedSkills);
        assertEquals(memberAggregatedSkills.getUserId(), userId);
        assertEquals(memberAggregatedSkills.getUserHandle(), handle);
        assertEquals(memberAggregatedSkills.getSkills().get(150L).getHidden(), false);
        assertEquals(memberAggregatedSkills.getSkills().get(150L).getSources(),
                new HashSet<String>(Arrays.asList("USER_ENTERED")));
    }
}