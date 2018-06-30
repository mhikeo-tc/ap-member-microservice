package com.appirio.service.test.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.*;
import com.appirio.service.member.api.CopilotStats;
import com.appirio.service.member.api.MemberStats;
import com.appirio.service.member.dao.MemberStatsDAO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/12/15.
 */
public class MemberStatsDAOTest {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberStatsDAOTest.class);

    private MemberStatsDAO memberStatsDAO;

    private static final boolean MOCK_DB = true;

    private static final String MemberStatsTable = "MemberStats";

    private static final DynamoDBMapper mapper = PowerMockito.mock(DynamoDBMapper.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private DynamoDbTableUtil dynamoDbTableUtil = new DynamoDbTableUtil();

    private MemberStats buildMemberStats() {
        CopilotStats copilotStats = new CopilotStats();
        copilotStats.setProjects(10L);
        copilotStats.setContests(10L);

        MemberStats memberStats = new MemberStats();
        memberStats.setUserId(userId);
        memberStats.setHandle("Ghostar");
        memberStats.setCopilot(copilotStats);
        return memberStats;
    }

    @Before
    public void init() {

        if (MOCK_DB) {
            MemberStats memberStats = buildMemberStats();
            when(mapper.load(MemberStats.class, userId)).thenReturn(memberStats);
            this.memberStatsDAO = new MemberStatsDAO(mapper);
        }
        else {
            this.memberStatsDAO = new MemberStatsDAO(dynamoDbTableUtil.getMapper());

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


            dynamoDbTableUtil.createDynamoDbTable(MemberStatsTable, keySchema, attributeDefinitions, handleLowerIndex);
            dynamoDbTableUtil.getDynamoDBTableInformation(MemberStatsTable);

            Item item = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", handle)
                    .withString("handleLower", handle.toLowerCase())
                    .withString("COPILOT", "{\"contests\": 10,\"projects\": 10}");
            dynamoDbTableUtil.loadDynamoDbTable(MemberStatsTable, item);
        }
    }

    @Test
    public void testGetMemberStats() {

        logger.debug("Get member stats for " + MemberStatsTable);
        MemberStats memberStats = memberStatsDAO.getMemberStats(userId);
        if (MOCK_DB) {
            verify(mapper).load(MemberStats.class, userId);
        }
        assertNotNull(memberStats);
        assertEquals(memberStats.getUserId(), userId);
        assertEquals(memberStats.getHandle(), handle);
        assertEquals(memberStats.getCopilot().getContests(), Long.valueOf(10));
        assertEquals(memberStats.getCopilot().getProjects(), Long.valueOf(10));
    }

    @After
    public void deleteMemberStatsTable() {

        if (!MOCK_DB) {
            dynamoDbTableUtil.deleteDynamoDbTable(MemberStatsTable);
        }
    }
}