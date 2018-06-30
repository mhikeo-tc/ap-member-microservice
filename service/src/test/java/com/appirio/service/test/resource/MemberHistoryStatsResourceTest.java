package com.appirio.service.test.resource;

import com.appirio.service.member.api.DevelopHistorySubTrack;
import com.appirio.service.member.api.DevelopHistoryTrack;
import com.appirio.service.member.api.MemberHistoryStats;
import com.appirio.service.member.manager.MemberHistoryStatsManager;
import com.appirio.service.member.resources.MemberHistoryStatsResource;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/23/15.
 */
public class MemberHistoryStatsResourceTest {

    private static final MemberHistoryStatsManager memberHistoryStatsManager = PowerMockito.mock(
            MemberHistoryStatsManager.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private static final MemberHistoryStatsResource memberHistoryStatsResource = new MemberHistoryStatsResource
            (memberHistoryStatsManager);

    private MemberHistoryStats buildMemberHistoryStats() {
        DevelopHistorySubTrack developHistorySubTrack = new DevelopHistorySubTrack();
        developHistorySubTrack.setName("DESIGN");
        developHistorySubTrack.setId(112L);

        List<DevelopHistorySubTrack> developHistorySubTracks = new ArrayList<DevelopHistorySubTrack>();
        developHistorySubTracks.add(developHistorySubTrack);
        DevelopHistoryTrack developHistoryTrack = new DevelopHistoryTrack();
        developHistoryTrack.setSubTracks(developHistorySubTracks);

        MemberHistoryStats memberHistoryStats = new MemberHistoryStats();
        memberHistoryStats.setUserId(userId);
        memberHistoryStats.setHandle(handle);
        memberHistoryStats.setDevelop(developHistoryTrack);

        return memberHistoryStats;
    }

    @Before
    public void init() throws SupplyException {

        MemberHistoryStats memberHistoryStats = buildMemberHistoryStats();
        memberHistoryStats.setUserId(userId);
        when(memberHistoryStatsManager.getMemberHistoryStats(handle)).thenReturn(memberHistoryStats);
    }

    @Test
    public void testGetMemberHistoryStats() throws SupplyException {

        ApiResponse apiResponse = memberHistoryStatsResource.getMemberHistoryStats(handle, null);

        verify(memberHistoryStatsManager).getMemberHistoryStats(anyString());
        MemberHistoryStats memberHistoryStats = (MemberHistoryStats) apiResponse.getResult().getContent();

        assertNotNull(memberHistoryStats);
        assertEquals(memberHistoryStats.getUserId(), userId);
        assertEquals(memberHistoryStats.getHandle(), handle);
        assertEquals(memberHistoryStats.getDevelop().getSubTracks().get(0).getName(), "DESIGN");
        assertEquals(memberHistoryStats.getDevelop().getSubTracks().get(0).getId(), Long.valueOf(112));
    }
}
