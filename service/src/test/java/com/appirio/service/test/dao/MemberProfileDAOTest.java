package com.appirio.service.test.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.*;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.MemberProfileAddress;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.test.dao.util.DynamoDbTableUtil;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/12/15.
 */
public class MemberProfileDAOTest extends GenericDAOTest {

    private MemberProfileDAO memberProfileDAO;

    private static final boolean MOCK_DB = true;

    private static final String MemberProfileTable = "MemberProfile";

    private static final DynamoDBMapper mapper = PowerMockito.mock(DynamoDBMapper.class);

    private static final PaginatedQueryList<MemberProfile> paginatedQueryList = PowerMockito.mock(PaginatedQueryList.class);

    private static final Integer userId = 151743;

    private static final String domain = "api.topcoder-dev.com";

    private static final String handle = "Ghostar";

    private static final Long token = 12345L;

    private static final String photoURL =  "https://" + domain.split("\\.")[1] + "-media.s3.amazonaws.com/member/profile/" +
            handle + "-" + token + ".jpg";

    private static final MemberProfile updateMemberProfile = PowerMockito.mock(MemberProfile.class);

    private static final List<String> validationMessages = PowerMockito.mock(List.class);

    private DynamoDbTableUtil dynamoDbTableUtil = new DynamoDbTableUtil();

    private MemberProfile buildMemberProfile() {

        MemberProfileAddress memberProfileAddress = new MemberProfileAddress();
        memberProfileAddress.setCity("San Francisco");
        memberProfileAddress.setStateCode("CA");
        memberProfileAddress.setZip("94101");
        memberProfileAddress.setStreetAddr1("123");
        memberProfileAddress.setStreetAddr2("xyz");
        memberProfileAddress.setType("HOME");

        List<MemberProfileAddress> addressList = new ArrayList<MemberProfileAddress>();
        addressList.add(memberProfileAddress);

        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle("Ghostar");
        memberProfile.setFirstName("Ghostar");
        memberProfile.setLastName("G");
        memberProfile.setOtherLangName("Ghostar");
        memberProfile.setEmail("email@domain.com");
        memberProfile.setDescription("xyz");
        memberProfile.setHomeCountryCode("USA");
        memberProfile.setCompetitionCountryCode("USA");
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());

        memberProfile.setAddresses(addressList);
        memberProfile.setPhotoURL(photoURL);
        return memberProfile;
    }

    @Before
    public void init() throws IllegalAccessException, InvocationTargetException,
            InstantiationException, SupplyException, NoSuchMethodException {

        if (MOCK_DB) {
            MemberProfile memberProfile = buildMemberProfile();
            when(mapper.query(any(MemberProfile.class.getClass()), anyObject())).thenReturn(paginatedQueryList);
            when(paginatedQueryList.size()).thenReturn(1);
            when(paginatedQueryList.get(0)).thenReturn(memberProfile);

            PowerMockito.doNothing().when(mapper).save(updateMemberProfile);
            when(updateMemberProfile.validate()).thenReturn(validationMessages);
            when(updateMemberProfile.getAddresses()).thenReturn(null);
            when(validationMessages.size()).thenReturn(0);
            this.memberProfileDAO = new MemberProfileDAO(mapper);
        }
        else {
            this.memberProfileDAO = new MemberProfileDAO(dynamoDbTableUtil.getMapper());

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

            dynamoDbTableUtil.createDynamoDbTable(MemberProfileTable, keySchema, attributeDefinitions, handleLowerIndex);

            dynamoDbTableUtil.getDynamoDBTableInformation(MemberProfileTable);
            Item item = new Item()
                    .withPrimaryKey("userId", userId)
                    .withString("handle", handle)
                    .withString("handleLower", handle.toLowerCase())
                    .withString("email", "email@domain.com")
                    .withString("description", "xyz");

            dynamoDbTableUtil.loadDynamoDbTable(MemberProfileTable, item);
        }
    }

    @Test
    public void testGetMemberProfile() {

        MemberProfile memberProfile = memberProfileDAO.getMemberProfile(handle);
        if (MOCK_DB) {
            verify(mapper).query(any(), anyObject());
        }
        assertNotNull(memberProfile);
        assertEquals(memberProfile.getHandle(), handle);
        assertEquals(memberProfile.getEmail(), "email@domain.com");
    }

    @Test
    public void testUpdateMemberProfile() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, SupplyException, IllegalAccessException {

        if (MOCK_DB) {
            memberProfileDAO.updateMemberProfile(updateMemberProfile, anyBoolean());
            verify(mapper).save(updateMemberProfile);
        }

        MemberProfile memberProfile = memberProfileDAO.getMemberProfile(handle);
        assertNotNull(memberProfile);
        assertEquals(memberProfile.getDescription(), "xyz");
        assertEquals(memberProfile.getEmail(), "email@domain.com");
    }

    @After
    public void deleteMemberProfileTable() {

        if (!MOCK_DB) {
            dynamoDbTableUtil.deleteDynamoDbTable(MemberProfileTable);
        }
    }
}