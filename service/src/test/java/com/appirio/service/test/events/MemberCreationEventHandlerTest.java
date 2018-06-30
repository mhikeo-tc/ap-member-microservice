package com.appirio.service.test.events;

import com.appirio.eventsbus.api.client.EventProducer;
import com.appirio.eventsbus.api.client.util.jsonevent.Event;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.events.MemberCreationEventHandler;
import com.appirio.supply.SupplyException;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 12/9/15.
 */
public class MemberCreationEventHandlerTest {

    public static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    public static final EventProducer eventProducer = PowerMockito.mock(EventProducer.class);

    public static final Event event = PowerMockito.mock(Event.class);

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
            + "  \"active\": false,"
            + "  \"status\": \"U\","
            + "  \"modifiedBy\": null,"
            + "  \"modifiedAt\": null,"
            + "  \"createdBy\": null,"
            + "  \"createdAt\": null"
            + "}";

    @Before
    public void init() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, SupplyException, IllegalAccessException {
        when(event.getPayload()).thenReturn(userPayload);
        when(event.getJsonPayload()).thenReturn(userPayload);
        PowerMockito.doNothing().when(memberProfileDAO).updateMemberProfile(anyObject(), anyBoolean());
    }

    @Test
    public void testMemberCreationEventHandler() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, SupplyException, IllegalAccessException {

        MemberCreationEventHandler memberCreationEventHandler = new MemberCreationEventHandler(memberProfileDAO, eventProducer);
        memberCreationEventHandler.handleEvent(event);
        verify(memberProfileDAO).updateMemberProfile(anyObject(), anyBoolean());
    }
}
