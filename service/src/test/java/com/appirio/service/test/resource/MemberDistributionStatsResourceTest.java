package com.appirio.service.test.resource;

import com.appirio.service.member.api.MemberDistributionStats;
import com.appirio.service.member.api.SubTrackDistributionStats;
import com.appirio.service.member.manager.MemberDistributionStatsManager;
import com.appirio.service.member.resources.MemberDistributionStatsResource;
import com.appirio.service.test.BaseTest;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by rakeshrecharla on 8/23/15.
 */
public class MemberDistributionStatsResourceTest extends BaseTest {

    private static final MemberDistributionStatsManager memberDistributionStatsManager = PowerMockito.mock(
            MemberDistributionStatsManager.class);

    private static final String track = "DEVELOP";

    private static final String subTrack = "DESIGN";

    private static final MemberDistributionStatsResource memberDistributionStatsResource =
            new MemberDistributionStatsResource(memberDistributionStatsManager);

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
        when(memberDistributionStatsManager.getMemberDistributionStats(anyObject())).
                thenReturn(memberDistributionStats);
    }

    @Test
    public void testGetMemberDistributionStats() {

        ApiResponse apiResponse = memberDistributionStatsResource.getMemberDistributionStats(
                createQueryParam("track=DEVELOP&subTrack=DESIGN"), null);

        verify(memberDistributionStatsManager).getMemberDistributionStats(anyObject());
        MemberDistributionStats memberDistributionStats = (MemberDistributionStats) apiResponse.getResult().getContent();

        assertNotNull(memberDistributionStats);
        assertEquals(memberDistributionStats.getTrack(), track);
        assertEquals(memberDistributionStats.getSubTrack(), subTrack);
        assertEquals(memberDistributionStats.getDistribution().getRatingRange0To099(), Long.valueOf(1));
        assertEquals(memberDistributionStats.getDistribution().getRatingRange1000To1099(), Long.valueOf(10));
    }

}