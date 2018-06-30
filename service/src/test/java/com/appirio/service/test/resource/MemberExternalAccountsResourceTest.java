package com.appirio.service.test.resource;

import com.appirio.service.member.api.ExternalAccountsGithub;
import com.appirio.service.member.api.MemberExternalAccounts;
import com.appirio.service.member.manager.MemberExternalAccountsManager;
import com.appirio.service.member.resources.MemberExternalAccountsResource;
import com.appirio.supply.SupplyException;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 9/12/15.
 */
public class MemberExternalAccountsResourceTest  {

    private static final MemberExternalAccountsManager memberExternalAccountsManager = PowerMockito.mock(
            MemberExternalAccountsManager.class);

    private static final Integer userId = 23226586;

    private static final String handle = "kohata";

    private static final String profileURL = "https://github.com/kohata";

    private static final MemberExternalAccountsResource memberExternalAccountsResource = new
            MemberExternalAccountsResource(memberExternalAccountsManager);

    private MemberExternalAccounts buildMemberExternalAccounts() {

        ExternalAccountsGithub externalAccountsGithub = new ExternalAccountsGithub();
        externalAccountsGithub.setHandle(handle);
        externalAccountsGithub.setProfileUrl(profileURL);
        externalAccountsGithub.setPublicRepos(1L);
        externalAccountsGithub.setFollowers(0L);
        externalAccountsGithub.setLanguages("Java");

        MemberExternalAccounts memberExternalAccounts = new MemberExternalAccounts();
        memberExternalAccounts.setUserId(userId);
        memberExternalAccounts.setHandle(handle);
        memberExternalAccounts.setGithub(externalAccountsGithub);

        return memberExternalAccounts;
    }

    @Before
    public void init() throws SupplyException, InterruptedException {

        MemberExternalAccounts memberExternalAccounts = buildMemberExternalAccounts();
        when(memberExternalAccountsManager.getMemberExternalAccounts(handle)).thenReturn(memberExternalAccounts);
    }

    @Test
    public void testGetMemberExternalAccounts() throws SupplyException, InterruptedException {

        ApiResponse apiResponse = memberExternalAccountsResource.getMemberExternalAccounts(handle, null);
        verify(memberExternalAccountsManager).getMemberExternalAccounts(anyString());
        MemberExternalAccounts memberExternalAccounts = (MemberExternalAccounts) apiResponse.getResult().getContent();

        assertNotNull(memberExternalAccounts);
        assertEquals(memberExternalAccounts.getUserId(), userId);
        assertEquals(memberExternalAccounts.getHandle(), handle);
        assertEquals(memberExternalAccounts.getGithub().getFollowers(), Long.valueOf(0));
        assertEquals(memberExternalAccounts.getGithub().getHandle(), handle);
        assertEquals(memberExternalAccounts.getGithub().getProfileUrl(), profileURL);
        assertEquals(memberExternalAccounts.getGithub().getPublicRepos(), Long.valueOf(1));
        assertEquals(memberExternalAccounts.getGithub().getLanguages(), "Java");
    }
}
