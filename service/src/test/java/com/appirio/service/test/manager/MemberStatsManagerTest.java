package com.appirio.service.test.manager;

import com.appirio.service.member.api.CopilotStats;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.MemberStats;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberStatsDAO;
import com.appirio.service.member.manager.MemberStatsManager;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/14/15.
 */
public class MemberStatsManagerTest {

    private static final MemberStatsDAO memberStatsDAO = PowerMockito.mock(MemberStatsDAO.class);

    private static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    private static final MemberStatsManager memberStatsManager = new MemberStatsManager(memberStatsDAO, memberProfileDAO);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private MemberProfile buildMemberProfile() {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle(handle);
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());
        return memberProfile;
    }

    private MemberStats buildMemberStats() {
        CopilotStats copilotStats = new CopilotStats();
        copilotStats.setContests(2264L);
        copilotStats.setProjects(90L);

        MemberStats memberStats = new MemberStats();
        memberStats.setUserId(userId);
        memberStats.setHandle(handle);
        memberStats.setCopilot(copilotStats);
        return memberStats;
    }

    @Before
    public void init() throws SupplyException {

        MemberProfile memberProfile = buildMemberProfile();
        MemberStats memberStats = buildMemberStats();
        when(memberProfileDAO.validateHandle(anyString(), anyObject(), anyBoolean())).thenReturn(memberProfile);
        when(memberStatsDAO.getMemberStats(userId)).thenReturn(memberStats);
    }

    @Test
    public void testGetMemberStats() throws SupplyException {

        MemberStats memberStats = memberStatsManager.getMemberStats(handle);
        verify(memberStatsDAO).getMemberStats(anyInt());

        assertNotNull(memberStats);
        assertEquals(memberStats.getUserId(), userId);
        assertEquals(memberStats.getHandle(), handle);
        assertEquals(memberStats.getCopilot().getContests(), Long.valueOf(2264));
        assertEquals(memberStats.getCopilot().getProjects(), Long.valueOf(90));
    }
}
