package com.appirio.service.test.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.*;
import com.appirio.service.member.api.DevelopHistorySubTrack;
import com.appirio.service.member.api.DevelopHistoryTrack;
import com.appirio.service.member.api.MemberHistoryStats;
import com.appirio.service.member.dao.MemberHistoryStatsDAO;
import com.appirio.service.test.dao.util.DynamoDbTableUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/23/15.
 */
public class MemberHistoryStatsDAOTest {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberHistoryStatsDAOTest.class);

    private MemberHistoryStatsDAO memberHistoryStatsDAO;

    private static final boolean MOCK_DB = true;

    private static final String MemberHistoryStatsTable = "MemberStatsHistory";

    private static final DynamoDBMapper mapper = PowerMockito.mock(DynamoDBMapper.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private DynamoDbTableUtil dynamoDbTableUtil = new DynamoDbTableUtil();

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
        memberHistoryStats.setHandle("Ghostar");
        memberHistoryStats.setDevelop(developHistoryTrack);

        return memberHistoryStats;
    }

    @Before
    public void init() {

        if (MOCK_DB) {
            MemberHistoryStats memberHistoryStats = buildMemberHistoryStats();

            when(mapper.load(MemberHistoryStats.class, userId)).thenReturn(memberHistoryStats);
            this.memberHistoryStatsDAO = new MemberHistoryStatsDAO(mapper);
        }
        else {
            this.memberHistoryStatsDAO = new MemberHistoryStatsDAO(dynamoDbTableUtil.getMapper());

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("userId")
                    .withAttributeType("N"));
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("handleLower")
                    .withAttributeType("S"));

            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName("userId")
                    .withKeyType(KeyType.HASH));

            GlobalSecondaryIndex handleLowerIndex = new GlobalSecondaryIndex()
                    .withIndexName("handleLower-index")
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(1L)
                            .withWriteCapacityUnits(1L))
                    .withKeySchema( new KeySchemaElement()
                            .withAttributeName("handleLower")
                            .withKeyType(KeyType.HASH))
                    .withProjection(new Projection()
                            .withProjectionType("ALL"));

            dynamoDbTableUtil.createDynamoDbTable(MemberHistoryStatsTable, keySchema,
                    attributeDefinitions, handleLowerIndex);
            dynamoDbTableUtil.getDynamoDBTableInformation(MemberHistoryStatsTable);

            Item item = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", handle)
                    .withString("handleLower", handle.toLowerCase())
                    .withString("DEVELOP", "{\"subTracks\":[{\"name\":\"DESIGN\",\"id\":112}]}");

            dynamoDbTableUtil.loadDynamoDbTable(MemberHistoryStatsTable, item);
        }
    }

    @Test
    public void testGetMemberHistoryStats() {

        logger.debug("Get member history stats for " + MemberHistoryStatsTable);
        MemberHistoryStats memberHistoryStats = memberHistoryStatsDAO.getMemberHistoryStats(userId);
        if (MOCK_DB) {
            verify(mapper).load(MemberHistoryStats.class, userId);
        }
        assertNotNull(memberHistoryStats);
        assertEquals(memberHistoryStats.getHandle(), handle);
        assertEquals(memberHistoryStats.getDevelop().getSubTracks().get(0).getName(), "DESIGN");
        assertEquals(memberHistoryStats.getDevelop().getSubTracks().get(0).getId(), Long.valueOf(112));
    }

    @After
    public void deleteMemberHistoryStatsTable() {

        if (!MOCK_DB) {
            dynamoDbTableUtil.deleteDynamoDbTable(MemberHistoryStatsTable);
        }
    }
}