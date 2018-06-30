package com.appirio.service.test.manager;

import com.appirio.service.member.api.DevelopHistorySubTrack;
import com.appirio.service.member.api.DevelopHistoryTrack;
import com.appirio.service.member.api.MemberHistoryStats;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberHistoryStatsDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberHistoryStatsManager;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/23/15.
 */
public class MemberHistoryStatsManagerTest {

    private static final MemberHistoryStatsDAO memberHistoryStatsDAO = PowerMockito.mock(MemberHistoryStatsDAO.class);

    private static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    private static final MemberHistoryStatsManager memberHistoryStatsManager = new MemberHistoryStatsManager(
            memberHistoryStatsDAO, memberProfileDAO);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private MemberProfile buildMemberProfile() {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle(handle);
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());
        return memberProfile;
    }

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

        MemberProfile memberProfile = buildMemberProfile();
        MemberHistoryStats memberHistoryStats = buildMemberHistoryStats();
        when(memberProfileDAO.validateHandle(anyString(), anyObject(), anyBoolean())).thenReturn(memberProfile);
        when(memberHistoryStatsDAO.getMemberHistoryStats(userId)).thenReturn(memberHistoryStats);
    }

    @Test
    public void testGetMemberHistoryStats() throws SupplyException {

        MemberHistoryStats memberHistoryStats = memberHistoryStatsManager.getMemberHistoryStats(handle);
        verify(memberHistoryStatsDAO).getMemberHistoryStats(anyInt());

        assertNotNull(memberHistoryStats);
        assertEquals(memberHistoryStats.getUserId(), userId);
        assertEquals(memberHistoryStats.getHandle(), handle);
        assertEquals(memberHistoryStats.getDevelop().getSubTracks().get(0).getName(), "DESIGN");
        assertEquals(memberHistoryStats.getDevelop().getSubTracks().get(0).getId(), Long.valueOf(112));
    }
}
