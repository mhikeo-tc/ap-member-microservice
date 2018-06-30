package com.appirio.service.test.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.*;
import com.appirio.service.member.api.MemberAggregatedSkills;
import com.appirio.service.member.api.MemberEnteredSkills;
import com.appirio.service.member.api.MemberInputSkills;
import com.appirio.service.member.api.MemberSkills;
import com.appirio.service.member.dao.MemberSkillsDAO;
import com.appirio.service.member.manager.MemberSkillsManager;
import com.appirio.service.test.dao.util.DynamoDbTableUtil;
import com.appirio.supply.SupplyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 9/12/15.
 */
public class MemberSkillsDAOTest {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberSkillsDAOTest.class);

    private MemberSkillsDAO memberSkillsDAO;

    private static final boolean MOCK_DB = true;

    private static final String MemberEnteredSkillsTable = "MemberEnteredSkills";

    private static final String MemberAggregatedSkillsTable = "MemberAggregatedSkills";

    private static final DynamoDBMapper mapper = PowerMockito.mock(DynamoDBMapper.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private DynamoDbTableUtil dynamoDbTableUtil = new DynamoDbTableUtil();

    private static final Map<Long, String> tagMap = new HashMap<>();

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
    public void init() {

        if (MOCK_DB) {
            MemberEnteredSkills memberEnteredSkills = buildMemberEnteredSkills();
            MemberAggregatedSkills memberAggregatedSkills = buildMemberAggregatedSkills();

            when(mapper.load(MemberEnteredSkills.class, userId)).thenReturn(memberEnteredSkills);
            when(mapper.load(MemberAggregatedSkills.class, userId)).thenReturn(memberAggregatedSkills);
            PowerMockito.doNothing().when(mapper).save(memberEnteredSkills);
            this.memberSkillsDAO = new MemberSkillsDAO(mapper);
        }
        else {
            this.memberSkillsDAO = new MemberSkillsDAO(dynamoDbTableUtil.getMapper());

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

            dynamoDbTableUtil.createDynamoDbTable(MemberEnteredSkillsTable, keySchema,
                    attributeDefinitions, handleLowerIndex);
            dynamoDbTableUtil.getDynamoDBTableInformation(MemberEnteredSkillsTable);

            Item memberEnteredItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("userHandle", handle)
                    .withString("handleLower", handle.toLowerCase())
                    .withString("skills", "{ \"150\" : {\"hidden\" : false}}");

            dynamoDbTableUtil.loadDynamoDbTable(MemberEnteredSkillsTable, memberEnteredItem);

            dynamoDbTableUtil.createDynamoDbTable(MemberAggregatedSkillsTable, keySchema,
                    attributeDefinitions, handleLowerIndex);

            dynamoDbTableUtil.getDynamoDBTableInformation(MemberAggregatedSkillsTable);

            Item memberAggregatedItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("userHandle", handle)
                    .withString("handleLower", handle.toLowerCase())
                    .withString("skills", "{\"251\" : {\"score\":98.0, " +
                            "\"sources\":[\"CHALLENGE\"], \"hidden\":false}}");

            dynamoDbTableUtil.loadDynamoDbTable(MemberAggregatedSkillsTable, memberAggregatedItem);
        }
        tagMap.put(150L, "Commerce Server 2009");
        MemberSkillsManager.setTagMap(tagMap);
    }

    @Test
    public void testGetMemberEnteredSkills() {

        logger.debug("Get member skills for " + MemberEnteredSkillsTable);

        MemberEnteredSkills memberEnteredSkills = memberSkillsDAO.getMemberEnteredSkills(userId);
        if (MOCK_DB) {
            verify(mapper).load(MemberEnteredSkills.class, userId);
        }
        assertNotNull(memberEnteredSkills);
        assertEquals(memberEnteredSkills.getUserId(), userId);
        assertEquals(memberEnteredSkills.getUserHandle(), handle);
        assertEquals(memberEnteredSkills.getSkills().get(150L).getHidden(), false);
    }

    @Test
    public void testGetMemberAggregatedSkills() {

        logger.debug("Get member skills for  + MemberAggregatedSkillsTable" +
                " and " + MemberEnteredSkillsTable);

        MemberAggregatedSkills memberAggregatedSkills = memberSkillsDAO.getMemberAggregatedSkills(userId);
        if (MOCK_DB) {
            verify(mapper).load(MemberAggregatedSkills.class, userId);
        }
        assertNotNull(memberAggregatedSkills);
        assertEquals(memberAggregatedSkills.getUserId(), userId);
        assertEquals(memberAggregatedSkills.getUserHandle(), handle);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getHidden(), false);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getScore(),
                Double.valueOf(98));
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getSources(),
                new HashSet<String>(Arrays.asList("CHALLENGE")));
    }

    @Test
    public void testUpdateMemberSkills() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, SupplyException, IllegalAccessException {

        MemberEnteredSkills memberEnteredSkills = buildMemberEnteredSkills();
        memberSkillsDAO.updateMemberSkills(memberEnteredSkills);

        MemberAggregatedSkills memberAggregatedSkills = memberSkillsDAO.getMemberAggregatedSkills(userId);
        assertNotNull(memberAggregatedSkills);
        assertEquals(memberAggregatedSkills.getUserId(), userId);
        assertEquals(memberAggregatedSkills.getUserHandle(), handle);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getHidden(), false);
        assertEquals(memberAggregatedSkills.getSkills().get(251L).getSources(),
                new HashSet<String>(Arrays.asList("CHALLENGE")));
    }

    @After
    public void testDeleteMemberSkills() {

        if (!MOCK_DB) {
            dynamoDbTableUtil.deleteDynamoDbTable(MemberEnteredSkillsTable);
            dynamoDbTableUtil.deleteDynamoDbTable(MemberAggregatedSkillsTable);
        }
    }

}