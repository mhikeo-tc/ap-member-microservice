package com.appirio.service.test.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.appirio.service.member.api.MemberDistributionStats;
import com.appirio.service.member.api.SubTrackDistributionStats;
import com.appirio.service.member.dao.MemberDistributionStatsDAO;
import com.appirio.service.test.dao.util.DynamoDbTableUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * Created by rakeshrecharla on 8/23/15.
 */
public class MemberDistributionStatsDAOTest {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberDistributionStatsDAOTest.class);

    private MemberDistributionStatsDAO memberDistributionStatsDAO;

    private static final boolean MOCK_DB = true;

    private static final String MemberDistributionStatsTable = "RatingsDistribution";

    private static final DynamoDBMapper mapper = PowerMockito.mock(DynamoDBMapper.class);

    private static final String track = "DEVELOP";

    private static final String subTrack = "ARCHITECTURE";

    private DynamoDbTableUtil dynamoDbTableUtil = new DynamoDbTableUtil();

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

        if (MOCK_DB) {
            MemberDistributionStats memberDistributionStats = buildMemberDistributionStats();
            this.memberDistributionStatsDAO = new MemberDistributionStatsDAO(mapper);
            when(mapper.load(MemberDistributionStats.class, track, subTrack)).thenReturn(memberDistributionStats);
        }
        else {
            this.memberDistributionStatsDAO = new MemberDistributionStatsDAO(dynamoDbTableUtil.getMapper());

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("track")
                    .withAttributeType("S"));
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("subTrack")
                    .withAttributeType("S"));

            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName("track")
                    .withKeyType(KeyType.HASH));
            keySchema.add(new KeySchemaElement()
                    .withAttributeName("subTrack")
                    .withKeyType(KeyType.RANGE));

            dynamoDbTableUtil.createDynamoDbTable(MemberDistributionStatsTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(MemberDistributionStatsTable);

            Item item = new Item()
                    .withPrimaryKey("track", track, "subTrack", subTrack)
                    .withString("distribution", "{\"ratingRange0To099\":1,\"ratingRange1000To1099\":10}");

            dynamoDbTableUtil.loadDynamoDbTable(MemberDistributionStatsTable, item);
        }
    }

    @Test
    public void testGetMemberDistributionStats() {

        logger.debug("Get member distribution stats for " + MemberDistributionStatsTable);
        MemberDistributionStats memberDistributionStats = memberDistributionStatsDAO.getMemberDistributionStats(track, subTrack);
        if (MOCK_DB) {
            verify(mapper).load(any(), anyString(), anyString());
        }

        assertNotNull(memberDistributionStats);
        assertEquals(memberDistributionStats.getTrack(), track);
        assertEquals(memberDistributionStats.getSubTrack(), subTrack);
        assertEquals(memberDistributionStats.getDistribution().getRatingRange0To099(), Long.valueOf(1));
        assertEquals(memberDistributionStats.getDistribution().getRatingRange1000To1099(), Long.valueOf(10));
    }

    @After
    public void deleteMemberDistributionStatsTable() {

        if (!MOCK_DB) {
            dynamoDbTableUtil.deleteDynamoDbTable(MemberDistributionStatsTable);
        }
    }
}
