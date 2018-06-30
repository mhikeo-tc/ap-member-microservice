package com.appirio.service.test.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.appirio.service.member.api.*;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 9/12/15.
 */
public class MemberExternalAccountsDAOTest {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalAccountsDAOTest.class);

    private MemberExternalAccountsDAO memberExternalAccountsDAO;

    private static final boolean MOCK_DB = true;

    private static final String ExternalAccountsTable = "Externals.Accounts";

    private static final String ExternalAccountsBehanceTable = "Externals.Behance";

    private static final String ExternalAccountsBitBucketTable = "Externals.BitBucket";

    private static final String ExternalAccountsDribbbleTable = "Externals.Dribbble";

    private static final String ExternalAccountsGitHubTable = "Externals.Github";

    private static final String ExternalAccountsLinkedInTable = "Externals.LinkedIn";

    private static final String ExternalAccountsStackOverflowTable = "Externals.Stackoverflow";

    private static final String ExternalAccountsTwitterTable = "Externals.Twitter";

    private static final DynamoDBMapper mapper = PowerMockito.mock(DynamoDBMapper.class);

    private static final Integer userId = 22688955;

    private static final String handle = "vikasrohit";

    private static final String profileURL = "https://github.com/vikasrohit";

    private DynamoDbTableUtil dynamoDbTableUtil = new DynamoDbTableUtil();

    private List<ExternalAccounts> buildExternalAccounts() {

        ExternalAccounts externalAccountsTwitter = new ExternalAccounts();
        externalAccountsTwitter.setUserId(userId.toString());
        externalAccountsTwitter.setAccountType("twitter");
        externalAccountsTwitter.setIsDeleted(true);

        ExternalAccounts externalAccountsGithub = new ExternalAccounts();
        externalAccountsGithub.setUserId(userId.toString());
        externalAccountsGithub.setAccountType("github");
        externalAccountsGithub.setIsDeleted(false);

        List<ExternalAccounts> externalAccountsList = null;
        if (MOCK_DB) {
            externalAccountsList = PowerMockito.mock(PaginatedQueryList.class);
            when(externalAccountsList.get(0)).thenReturn(externalAccountsGithub);
        }
        else {
            externalAccountsList = new ArrayList<ExternalAccounts>();
            externalAccountsList.add(externalAccountsGithub);
        }
        return externalAccountsList;
    }

    private ExternalAccountsBehance buildExternalAccountsBehance() {

        ExternalAccountsBehance externalAccountsBehance = new ExternalAccountsBehance();
        externalAccountsBehance.setUserId(userId);
        externalAccountsBehance.setName("Faydi");
        externalAccountsBehance.setProfileUrl(profileURL);
        externalAccountsBehance.setProjectAppreciations(5393L);
        externalAccountsBehance.setSummary("designed");
        externalAccountsBehance.setCreativeFields("Interaction Design");
        return  externalAccountsBehance;
    }

    private ExternalAccountsBitBucket buildExternalAccountsBitBucket() {

        ExternalAccountsBitBucket externalAccountsBitBucket = new ExternalAccountsBitBucket();
        externalAccountsBitBucket.setUserId(userId);
        externalAccountsBitBucket.setFollowers(0L);
        externalAccountsBitBucket.setHandle(handle);
        externalAccountsBitBucket.setLanguages("html/css");
        externalAccountsBitBucket.setProfileUrl(profileURL);
        externalAccountsBitBucket.setRepos(1L);
        return externalAccountsBitBucket;
    }

    private ExternalAccountsDribbble buildExternalAccountsDribbble() {

        ExternalAccountsDribbble externalAccountsDribbble = new ExternalAccountsDribbble();
        externalAccountsDribbble.setUserId(userId);
        externalAccountsDribbble.setHandle("simplebits");
        externalAccountsDribbble.setFollowers(0L);
        externalAccountsDribbble.setName("Dan");
        externalAccountsDribbble.setLikes(39728L);
        externalAccountsDribbble.setProfileUrl(profileURL);
        return externalAccountsDribbble;
    }

    private ExternalAccountsGithub buildExternalAccountsGithub() {

        ExternalAccountsGithub externalAccountsGithub = new ExternalAccountsGithub();
        externalAccountsGithub.setUserId(userId);
        externalAccountsGithub.setHandle(handle);
        externalAccountsGithub.setProfileUrl(profileURL);
        externalAccountsGithub.setPublicRepos(1L);
        externalAccountsGithub.setFollowers(0L);
        externalAccountsGithub.setLanguages("Java");
        return externalAccountsGithub;
    }

    private ExternalAccountsLinkedIn buildExternalAccountsLinkedIn() {

        ExternalAccountsLinkedIn externalAccountsLinkedIn = new ExternalAccountsLinkedIn();
        externalAccountsLinkedIn.setUserId(userId);
        externalAccountsLinkedIn.setHandle(handle);
        externalAccountsLinkedIn.setSocialId("100");
        externalAccountsLinkedIn.setSummary("Engineer");
        externalAccountsLinkedIn.setTitle("Principal Engineer");
        externalAccountsLinkedIn.setProfileUrl(profileURL);
        return externalAccountsLinkedIn;
    }

    private ExternalAccountsStackOverflow buildExternalAccountsStackOverflow() {

        ExternalAccountsStackOverflow externalAccountsStackOverflow = new ExternalAccountsStackOverflow();
        externalAccountsStackOverflow.setUserId(userId);
        externalAccountsStackOverflow.setName(handle);
        externalAccountsStackOverflow.setSocialId("365172");
        externalAccountsStackOverflow.setAnswers(42L);
        externalAccountsStackOverflow.setQuestions(9L);
        externalAccountsStackOverflow.setReputation(938L);
        externalAccountsStackOverflow.setProfileUrl(profileURL);
        return externalAccountsStackOverflow;
    }

    private ExternalAccountsTwitter buildExternalAccountsTwitter() {

        ExternalAccountsTwitter externalAccountsTwitter = new ExternalAccountsTwitter();
        externalAccountsTwitter.setUserId(userId);
        externalAccountsTwitter.setHandle(handle);
        externalAccountsTwitter.setSocialId("100");
        externalAccountsTwitter.setBio("Engineer");
        externalAccountsTwitter.setNoOfTweets(263L);
        externalAccountsTwitter.setProfileUrl(profileURL);
        return externalAccountsTwitter;
    }

    @Before
    public void init() {

        if (MOCK_DB) {
            List<ExternalAccounts> externalAccountsList = buildExternalAccounts();
            ExternalAccountsBehance externalAccountsBehance = buildExternalAccountsBehance();
            ExternalAccountsBitBucket externalAccountsBitBucket = buildExternalAccountsBitBucket();
            ExternalAccountsDribbble externalAccountsDribbble = buildExternalAccountsDribbble();
            ExternalAccountsGithub externalAccountsGithub = buildExternalAccountsGithub();
            ExternalAccountsLinkedIn externalAccountsLinkedIn = buildExternalAccountsLinkedIn();
            ExternalAccountsStackOverflow externalAccountsStackOverflow = buildExternalAccountsStackOverflow();
            ExternalAccountsTwitter externalAccountsTwitter = buildExternalAccountsTwitter();

            when(mapper.query(any(ExternalAccounts.class.getClass()), anyObject()))
                    .thenReturn(externalAccountsList);
            when(mapper.load(ExternalAccountsBehance.class, userId)).thenReturn(externalAccountsBehance);
            when(mapper.load(ExternalAccountsBitBucket.class, userId)).thenReturn(externalAccountsBitBucket);
            when(mapper.load(ExternalAccountsDribbble.class, userId)).thenReturn(externalAccountsDribbble);
            when(mapper.load(ExternalAccountsGithub.class, userId)).thenReturn(externalAccountsGithub);
            when(mapper.load(ExternalAccountsLinkedIn.class, userId)).thenReturn(externalAccountsLinkedIn);
            when(mapper.load(ExternalAccountsStackOverflow.class, userId)).thenReturn(externalAccountsStackOverflow);
            when(mapper.load(ExternalAccountsTwitter.class, userId)).thenReturn(externalAccountsTwitter);

            this.memberExternalAccountsDAO = new MemberExternalAccountsDAO(mapper);
        }
        else {
            this.memberExternalAccountsDAO = new MemberExternalAccountsDAO(dynamoDbTableUtil.getMapper());

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("userId")
                    .withAttributeType("S"));

            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName("userId")
                    .withKeyType(KeyType.HASH));

            // Create and load accounts table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsTable);

            Item accountsItem1 = new Item()
                    .withPrimaryKey("userId", userId.toString())
                    .withString("accountType", "twitter")
                    .withBoolean("isDeleted", true);
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsTable, accountsItem1);

            Item accountsItem2 = new Item()
                    .withPrimaryKey("userId", userId.toString())
                    .withString("accountType", "github")
                    .withBoolean("isDeleted", false);
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsTable, accountsItem2);


            // Set the attribute type to Numeric for other tables
            attributeDefinitions.get(0).setAttributeType("N");

            // Create and load behance table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsBehanceTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsBehanceTable);

            Item behanceItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("name", "Faydi")
                    .withString("profileUrl", profileURL)
                    .withLong("projectAppreciations", 5393)
                    .withString("summary", "designed")
                    .withString("creativeFields", "Interaction Design");
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsBehanceTable, behanceItem);


            // Create and load bitbucket table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsBitBucketTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsBitBucketTable);

            Item bitBucketItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withLong("followers", 0)
                    .withString("handle", handle)
                    .withString("languages", "html/css")
                    .withString("profileUrl", profileURL)
                    .withLong("repos", 1);
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsBitBucketTable, bitBucketItem);


            // Create and load dribbble table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsDribbbleTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsDribbbleTable);

            Item dribbleItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", "simplebits")
                    .withLong("followers", 0)
                    .withString("name", "Dan")
                    .withLong("likes", 39728)
                    .withString("profileUrl", profileURL);
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsDribbbleTable, dribbleItem);


            // Create and load github table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsGitHubTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsGitHubTable);

            Item gitHubItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", handle)
                    .withString("profileUrl", profileURL)
                    .withLong("publicRepos", 1)
                    .withLong("followers", 0)
                    .withString("languages", "Java");
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsGitHubTable, gitHubItem);


            // Create and load linkedIn table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsLinkedInTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsLinkedInTable);

            Item linkedInItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", handle)
                    .withString("socialId", "100")
                    .withString("summary", "Engineer")
                    .withString("title", "Principal Engineer")
                    .withString("profileUrl", profileURL);
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsLinkedInTable, linkedInItem);


            // Create and load stack overflow table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsStackOverflowTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsStackOverflowTable);

            Item stackOverflowItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("name", handle)
                    .withString("socialId", "365172")
                    .withLong("answers", 42)
                    .withLong("questions", 9)
                    .withLong("reputation", 938)
                    .withString("profileUrl", profileURL);
            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsStackOverflowTable, stackOverflowItem);


            // Create and load twitter table
            dynamoDbTableUtil.createDynamoDbTable(ExternalAccountsTwitterTable, keySchema, attributeDefinitions, null);
            dynamoDbTableUtil.getDynamoDBTableInformation(ExternalAccountsTwitterTable);

            Item twitterItem = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", handle)
                    .withString("socialId", "100")
                    .withString("bio", "Engineer")
                    .withLong("noOfTweets", 263)
                    .withString("profileUrl", profileURL);

            dynamoDbTableUtil.loadDynamoDbTable(ExternalAccountsTwitterTable, twitterItem);
        }
    }

    @Test
    public void testGetMemberExternalAccounts() {

        logger.debug("Get member external accounts " + ExternalAccountsTable);
        List<ExternalAccounts> externalAccountsList = memberExternalAccountsDAO.getExternalAccounts(userId.toString());
        if (MOCK_DB) {
            verify(mapper).query(any(ExternalAccounts.class.getClass()), anyObject());
        }
        assertNotNull(externalAccountsList);
        assertEquals(externalAccountsList.get(0).getUserId(), userId.toString());
        assertEquals(externalAccountsList.get(0).getAccountType(), "github");
        assertEquals(externalAccountsList.get(0).getIsDeleted(), false);
    }

    @Test
    public void testGetMemberExternalAccountsBehance() {

        logger.debug("Get member external accounts behance for " + ExternalAccountsBehanceTable);
        ExternalAccountsBehance externalAccountsBehance = memberExternalAccountsDAO.
                getMemberExternalAccountsBehance(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsBehance.class, userId);
        }
        assertNotNull(externalAccountsBehance);
        assertEquals(externalAccountsBehance.getUserId(), userId);
        assertEquals(externalAccountsBehance.getName(), "Faydi");
        assertEquals(externalAccountsBehance.getProfileUrl(), profileURL);
        assertEquals(externalAccountsBehance.getProjectAppreciations(), Long.valueOf(5393));
        assertEquals(externalAccountsBehance.getSummary(), "designed");
        assertEquals(externalAccountsBehance.getCreativeFields(), "Interaction Design");
    }

    @Test
    public void testGetMemberExternalAccountsBitBucket() {

        logger.debug("Get member external accounts bit bucket for " + ExternalAccountsBitBucketTable);
        ExternalAccountsBitBucket externalAccountsBitBucket = memberExternalAccountsDAO.
                getMemberExternalAccountsBitBucket(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsBitBucket.class, userId);
        }
        assertNotNull(externalAccountsBitBucket);
        assertEquals(externalAccountsBitBucket.getUserId(), userId);
        assertEquals(externalAccountsBitBucket.getFollowers(), Long.valueOf(0));
        assertEquals(externalAccountsBitBucket.getLanguages(), "html/css");
        assertEquals(externalAccountsBitBucket.getProfileUrl(), profileURL);
        assertEquals(externalAccountsBitBucket.getRepos(), Long.valueOf(1));
        assertEquals(externalAccountsBitBucket.getHandle(), handle);
    }

    @Test
    public void testGetMemberExternalAccountsDribbble() {

        logger.debug("Get member external accounts bit bucket for " + ExternalAccountsDribbbleTable);
        ExternalAccountsDribbble externalAccountsDribbble = memberExternalAccountsDAO.
                getMemberExternalAccountsDribbble(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsDribbble.class, userId);
        }
        assertNotNull(externalAccountsDribbble);
        assertEquals(externalAccountsDribbble.getUserId(), userId);
        assertEquals(externalAccountsDribbble.getHandle(), "simplebits");
        assertEquals(externalAccountsDribbble.getFollowers(), Long.valueOf(0));
        assertEquals(externalAccountsDribbble.getName(), "Dan");
        assertEquals(externalAccountsDribbble.getLikes(), Long.valueOf(39728));
        assertEquals(externalAccountsDribbble.getProfileUrl(), profileURL);
    }

    @Test
    public void testGetMemberExternalAccountsGithub() {

        logger.debug("Get member external accounts github for " + ExternalAccountsGitHubTable);
        ExternalAccountsGithub externalAccountsGithub = memberExternalAccountsDAO.
                getMemberExternalAccountsGithub(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsGithub.class, userId);
        }
        assertNotNull(externalAccountsGithub);
        assertEquals(externalAccountsGithub.getUserId(), userId);
        assertEquals(externalAccountsGithub.getProfileUrl(), profileURL);
        assertEquals(externalAccountsGithub.getPublicRepos(), Long.valueOf(1));
        assertEquals(externalAccountsGithub.getFollowers(), Long.valueOf(0));
        assertEquals(externalAccountsGithub.getLanguages(), "Java");
    }

    @Test
    public void testGetMemberExternalAccountsLinkedIn() {

        logger.debug("Get member external accounts github for " + ExternalAccountsLinkedInTable);
        ExternalAccountsLinkedIn externalAccountsLinkedIn = memberExternalAccountsDAO.
                getMemberExternalAccountsLinkedIn(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsLinkedIn.class, userId);
        }
        assertNotNull(externalAccountsLinkedIn);
        assertEquals(externalAccountsLinkedIn.getUserId(), userId);
        assertEquals(externalAccountsLinkedIn.getSocialId(), "100");
        assertEquals(externalAccountsLinkedIn.getSummary(), "Engineer");
        assertEquals(externalAccountsLinkedIn.getTitle(), "Principal Engineer");
        assertEquals(externalAccountsLinkedIn.getProfileUrl(), profileURL);
    }

    @Test
    public void testGetMemberExternalAccountsStackOverflow() {

        logger.debug("Get member external accounts stack overflow for " + ExternalAccountsStackOverflowTable);
        ExternalAccountsStackOverflow externalAccountsStackOverflow = memberExternalAccountsDAO.
                getMemberExternalAccountsStackOverflow(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsStackOverflow.class, userId);
        }
        assertNotNull(externalAccountsStackOverflow);
        assertEquals(externalAccountsStackOverflow.getUserId(), userId);
        assertEquals(externalAccountsStackOverflow.getName(), handle);
        assertEquals(externalAccountsStackOverflow.getSocialId(), "365172");
        assertEquals(externalAccountsStackOverflow.getAnswers(), Long.valueOf(42));
        assertEquals(externalAccountsStackOverflow.getQuestions(), Long.valueOf(9));
        assertEquals(externalAccountsStackOverflow.getReputation(), Long.valueOf(938L));
        assertEquals(externalAccountsStackOverflow.getProfileUrl(), profileURL);
    }

    @Test
    public void testGetMemberExternalAccountsTwitter() {

        logger.debug("Get member external accounts twitter for " + ExternalAccountsTwitterTable);
        ExternalAccountsTwitter externalAccountsTwitter = memberExternalAccountsDAO.
                getMemberExternalAccountsTwitter(userId);
        if (MOCK_DB) {
            verify(mapper).load(ExternalAccountsTwitter.class, userId);
        }
        assertNotNull(externalAccountsTwitter);
        assertEquals(externalAccountsTwitter.getUserId(), userId);
        assertEquals(externalAccountsTwitter.getSocialId(), "100");
        assertEquals(externalAccountsTwitter.getBio(), "Engineer");
        assertEquals(externalAccountsTwitter.getNoOfTweets(), Long.valueOf(263));
        assertEquals(externalAccountsTwitter.getProfileUrl(), profileURL);
    }

    @After
    public void testDeleteMemberExternalAccounts() {

        if (!MOCK_DB) {
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsBehanceTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsBitBucketTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsDribbbleTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsGitHubTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsLinkedInTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsStackOverflowTable);
            dynamoDbTableUtil.deleteDynamoDbTable(ExternalAccountsTwitterTable);
        }
    }
}