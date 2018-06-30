package com.appirio.service.test.events;

import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.util.jsonevent.Event;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.events.MemberActivationEventHandler;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 12/9/15.
 */
public class MemberActivationEventHandlerTest {

    public static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    public static final EventProducer eventProducer = PowerMockito.mock(EventProducer.class);

    public static final Event event = PowerMockito.mock(Event.class);

    public static final Integer userId = 159068;

    public static final String handle = "JohnDoe";

    public static final String userPayload = "{"
            + "  \"id\": \"159068\","
            + "  \"handle\": \"JohnDoe\","
            + "  \"email\": \"johndoe@appirio.com\","
            + "  \"firstName\": \"John\","
            + "  \"lastName\": \"Doe\","
            + "  \"credential\": {"
            + "    \"id\": null,"
            + "    \"modifiedBy\": null,"
            + "    \"modifiedAt\": null,"
            + "    \"createdBy\": null,"
            + "    \"createdAt\": null,"
            + "    \"activationCode\": \"13IPBGV6S2G\","
            + "    \"resetToken\": null"
            + "  },"
            + "  \"profile\": null,"
            + "  \"country\": {"
            + "    \"code\": \"840\","
            + "    \"name\": \"United States\""
            + "  },"
            + "  \"utmSource\": \"UTM_SOURCE\","
            + "  \"utmMedium\": \"UTM_MEDIUM\","
            + "  \"utmCampaign\": \"UTM_CAMPAIGN\","
            + "  \"active\": true,"
            + "  \"status\": \"A\","
            + "  \"modifiedBy\": null,"
            + "  \"modifiedAt\": null,"
            + "  \"createdBy\": null,"
            + "  \"createdAt\": null"
            + "}";

    MemberProfile buildMemberProfile() {

        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle(handle);
        memberProfile.setFirstName("John");
        memberProfile.setLastName("Doe");
        memberProfile.setEmail("johndoe@appirio.com");
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());

        return  memberProfile;
    }

    @Before
    public void init() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, SupplyException, IllegalAccessException {
        MemberProfile memberProfile = buildMemberProfile();
        when(event.getPayload()).thenReturn(userPayload);
        when(event.getJsonPayload()).thenReturn(userPayload);
        when(memberProfileDAO.getMemberProfile(anyString())).thenReturn(memberProfile);
        PowerMockito.doNothing().when(memberProfileDAO).updateMemberProfile(memberProfile, false);
    }

    @Test
    public void testMemberActivationEventHandler() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, SupplyException, IllegalAccessException {

        MemberActivationEventHandler memberActivationEventHandler = new MemberActivationEventHandler(memberProfileDAO, eventProducer);
        memberActivationEventHandler.handleEvent(event);
        verify(memberProfileDAO).getMemberProfile(anyString());
        verify(memberProfileDAO).updateMemberProfile(anyObject(), anyBoolean());
    }
}
