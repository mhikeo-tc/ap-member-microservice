package com.appirio.service.test.manager;

import com.appirio.service.member.api.MemberDistributionStats;
import com.appirio.service.member.api.SubTrackDistributionStats;
import com.appirio.service.member.dao.MemberDistributionStatsDAO;
import com.appirio.service.member.manager.MemberDistributionStatsManager;
import com.appirio.service.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/23/15.
 */
public class MemberDistributionStatsManagerTest extends BaseTest {

    private static final MemberDistributionStatsDAO memberDistributionStatsDAO = PowerMockito.mock(MemberDistributionStatsDAO.class);

    private static final MemberDistributionStatsManager memberDistributionStatsManager = new MemberDistributionStatsManager(memberDistributionStatsDAO);

    private static final String track = "DEVELOP";

    private static final String subTrack = "ARCHITECTURE";

    private MemberDistributionStats buildMemberDistributionStats() {

        SubTrackDistributionStats subTrackDistributionStats = new SubTrackDistributionStats();
        subTrackDistributionStats.setRatingRange0To099(1L);
        subTrackDistributionStats.setRatingRange1000To1099(10L);

        MemberDistributionStats memberDistributionStats = new MemberDistributionStats();
        memberDistributionStats.setTrack(track);
        memberDistributionStats.setSubTrack(subTrack);
        memberDistributionStats.setDistribution(subTrackDistributionStats);

        return memberDistributionStats;
    }

    @Before
    public void init() {

        MemberDistributionStats memberDistributionStats = buildMemberDistributionStats();
        when(memberDistributionStatsDAO.getMemberDistributionStats(track, subTrack)).thenReturn(memberDistributionStats);
    }

    @Test
    public void testGetMemberDistributionStats() {

        MemberDistributionStats memberDistributionStats = memberDistributionStatsManager.
                getMemberDistributionStats(createQueryParam("track=DEVELOP&subTrack=ARCHITECTURE"));
        verify(memberDistributionStatsDAO).getMemberDistributionStats(track, subTrack);

        assertNotNull(memberDistributionStats);
        assertEquals(memberDistributionStats.getTrack(), track);
        assertEquals(memberDistributionStats.getSubTrack(), subTrack);
        assertEquals(memberDistributionStats.getDistribution().getRatingRange0To099(), Long.valueOf(1));
        assertEquals(memberDistributionStats.getDistribution().getRatingRange1000To1099(), Long.valueOf(10));
    }
}
