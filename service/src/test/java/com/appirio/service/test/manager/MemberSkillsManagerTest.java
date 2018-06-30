package com.appirio.service.test.manager;

import com.appirio.service.member.api.*;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberSkillsDAO;
import com.appirio.service.member.manager.MemberSkillsManager;
import com.appirio.service.test.BaseTest;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
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
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MemberSkillsManagerTest extends BaseTest {

    private static final MemberSkillsDAO memberSkillsDAO = PowerMockito.mock(MemberSkillsDAO.class);

    private static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    private MemberSkillsManager memberSkillsManager;

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private static final String fileServiceDomain = "api.topcoder-dev.com";

    private static final String apiVersion = "v3";

    public MemberSkillsManagerTest() throws SupplyException {
        this.memberSkillsManager = new MemberSkillsManager(memberSkillsDAO, memberProfileDAO,
                fileServiceDomain, apiVersion);
        // add mock tag map
        Map<Long, String> tagMap = this.memberSkillsManager.getTagMap();
        if(!tagMap.containsKey(251L)){
            tagMap.put(251L, "mock tag for id 251L");
        }
        if(!tagMap.containsKey(150L)){
            tagMap.put(150L, "mock tag for id 150L");
        }
    }

    private MemberProfile buildMemberProfile() {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle(handle);
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());
        return memberProfile;
    }

    private MemberAggregatedSkills buildMemberAggregatedSkills() {

        MemberAggregatedSkills memberAggregatedSkills = new MemberAggregatedSkills();
        memberAggregatedSkills.setUserId(userId);
        memberAggregatedSkills.setUserHandle(handle);
        memberAggregatedSkills.setHandleLower(handle.toLowerCase());

        MemberSkills memberSkills = new MemberSkills();
        memberSkills.setScore(98.0);
        memberSkills.setHidden(false);
        memberSkills.setSources(new HashSet<String>(Arrays.asList("CHALLENGE")));

        Map<Long, MemberSkills> memberSkillsMap = new HashMap<Long, MemberSkills>();
        memberSkillsMap.put(251L, memberSkills);
        memberAggregatedSkills.setSkills(memberSkillsMap);

        return memberAggregatedSkills;
    }

    private MemberEnteredSkills buildMemberEnteredSkills() {

        MemberEnteredSkills memberEnteredSkills = new MemberEnteredSkills();
        memberEnteredSkills.setUserId(userId);
        memberEnteredSkills.setUserHandle(handle);
        memberEnteredSkills.setHandleLower(handle.toLowerCase());

        MemberInputSkills memberInputSkills = new MemberInputSkills();
        memberInputSkills.setHidden(false);

        Map<Long, MemberInputSkills> memberInputSkillsMap = new HashMap<Long, MemberInputSkills>();
        memberInputSkillsMap.put(150L, memberInputSkills);
        memberEnteredSkills.setSkills(memberInputSkillsMap);

        return memberEnteredSkills;
    }

    @Before
    public void init() throws IllegalAccessException, SupplyException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {

        MemberProfile memberProfile = buildMemberProfile();
        MemberAggregatedSkills memberAggregatedSkills = buildMemberAggregatedSkills();
        MemberEnteredSkills memberEnteredSkills = buildMemberEnteredSkills();

        when(memberProfileDAO.validateHandle(anyString(), anyObject(), anyBoolean())).thenReturn(memberProfile);
        when(memberSkillsDAO.getMemberAggregatedSkills(userId)).thenReturn(memberAggregatedSkills);
        when(memberSkillsDAO.getMemberEnteredSkills(userId)).thenReturn(memberEnteredSkills);
        PowerMockito.doNothing().when(memberSkillsDAO).updateMemberSkills(memberEnteredSkills);
    }

    @Test
    public void testGetMemberSkills() throws SupplyException {

        MemberAggregatedSkills memberAggregatedSkills = memberSkillsManager.getMemberAggregatedSkills(handle);
        verify(memberProfileDAO).validateHandle(anyString(), anyObject(), anyBoolean());
        verify(memberSkillsDAO).getMemberEnteredSkills(anyInt());
        verify(memberSkillsDAO).getMemberAggregatedSkills(anyInt());

        assertNotNull(memberAggregatedSkills);
        assertEquals(memberAggregatedSkills.getUserId(), userId);
        assertEquals(memberAggregatedSkills.getUserHandle(), handle);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getHidden(), false);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getSources(),
                new HashSet<String>(Arrays.asList("CHALLENGE")));
    }

    @Test
    public void testUpdateMemberSkills() throws SupplyException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {

        MemberEnteredSkills memberEnteredSkills = buildMemberEnteredSkills();
        MemberAggregatedSkills memberAggregatedSkills = memberSkillsManager.updateMemberSkills(handle,
                createUser(userId.toString()), memberEnteredSkills);

        verify(memberProfileDAO, times(3)).validateHandle(anyString(), anyObject(), anyBoolean());
        verify(memberSkillsDAO, times(3)).getMemberEnteredSkills(anyInt());
        verify(memberSkillsDAO, times(2)).getMemberAggregatedSkills(anyInt());
        verify(memberSkillsDAO).updateMemberSkills(anyObject());

        assertNotNull(memberAggregatedSkills);
        assertEquals(memberAggregatedSkills.getUserId(), userId);
        assertEquals(memberAggregatedSkills.getUserHandle(), handle);
        assertEquals(memberAggregatedSkills.getSkills().get(150L).getHidden(), false);
        assertEquals(memberAggregatedSkills.getSkills().get(150L).getSources(),
                new HashSet<String>(Arrays.asList("USER_ENTERED")));
    }
}
