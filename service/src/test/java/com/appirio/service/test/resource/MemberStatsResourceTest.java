package com.appirio.service.test.resource;

import com.appirio.service.member.api.CopilotStats;
import com.appirio.service.member.api.MemberStats;
import com.appirio.service.member.manager.MemberStatsManager;
import com.appirio.service.member.resources.MemberStatsResource;
import com.appirio.service.test.BaseTest;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/14/15.
 */
public class MemberStatsResourceTest extends BaseTest {

    private static final MemberStatsManager memberStatsManager = PowerMockito.mock(MemberStatsManager.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private static final MemberStatsResource memberStatsResource = new MemberStatsResource(memberStatsManager);

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

        MemberStats memberStats = buildMemberStats();
        when(memberStatsManager.getMemberStats(handle)).thenReturn(memberStats);
    }

    @Test
    public void testGetMemberStats() throws SupplyException {

        ApiResponse apiResponse = memberStatsResource.getMemberStats(handle, null);
        verify(memberStatsManager).getMemberStats(anyString());
        MemberStats memberStats = (MemberStats) apiResponse.getResult().getContent();

        assertNotNull(memberStats);
        assertEquals(memberStats.getUserId(), userId);
        assertEquals(memberStats.getHandle(), handle);
        assertEquals(memberStats.getCopilot().getContests(), Long.valueOf(2264));
        assertEquals(memberStats.getCopilot().getProjects(), Long.valueOf(90));
    }
}