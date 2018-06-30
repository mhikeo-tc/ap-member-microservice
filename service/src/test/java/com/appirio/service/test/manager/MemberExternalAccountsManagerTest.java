package com.appirio.service.test.manager;

import com.appirio.service.member.api.*;
import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.manager.MemberExternalAccountsManager;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by rakeshrecharla on 9/12/15.
 */
public class MemberExternalAccountsManagerTest {

    private static final MemberExternalAccountsDAO memberExternalAccountsDAO = PowerMockito.mock(
            MemberExternalAccountsDAO.class);

    private static final MemberProfileDAO memberProfileDAO = PowerMockito.mock(MemberProfileDAO.class);

    private static final MemberExternalAccountsManager memberExternalAccountsManager = new
            MemberExternalAccountsManager(memberExternalAccountsDAO, memberProfileDAO);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private static final String profileURL = "https://github.com/ghostar";

    private List<ExternalAccounts> buildExternalAccounts() {

        List<ExternalAccounts> externalAccountsList = new ArrayList<ExternalAccounts>();

        ExternalAccounts externalAccountsBehance =new ExternalAccounts();
        externalAccountsBehance.setUserId(userId.toString());
        externalAccountsBehance.setAccountType("behance");
        externalAccountsBehance.setIsDeleted(false);

        ExternalAccounts externalAccountsBitBucket = new ExternalAccounts();
        externalAccountsBitBucket.setUserId(userId.toString());
        externalAccountsBitBucket.setAccountType("bitbucket");
        externalAccountsBitBucket.setIsDeleted(false);

        ExternalAccounts externalAccountsDribbble = new ExternalAccounts();
        externalAccountsDribbble.setUserId(userId.toString());
        externalAccountsDribbble.setAccountType("dribbble");
        externalAccountsDribbble.setIsDeleted(false);

        ExternalAccounts externalAccountsGithub = new ExternalAccounts();
        externalAccountsGithub.setUserId(userId.toString());
        externalAccountsGithub.setAccountType("github");
        externalAccountsGithub.setIsDeleted(false);

        ExternalAccounts externalAccountsLinkedIn = new ExternalAccounts();
        externalAccountsLinkedIn.setUserId(userId.toString());
        externalAccountsLinkedIn.setAccountType("linkedin");
        externalAccountsLinkedIn.setIsDeleted(false);

        ExternalAccounts externalAccountsStackOverflow = new ExternalAccounts();
        externalAccountsStackOverflow.setUserId(userId.toString());
        externalAccountsStackOverflow.setAccountType("stackoverflow");
        externalAccountsStackOverflow.setIsDeleted(false);

        ExternalAccounts externalAccountsTwitter = new ExternalAccounts();
        externalAccountsTwitter.setUserId(userId.toString());
        externalAccountsTwitter.setAccountType("twitter");
        externalAccountsTwitter.setIsDeleted(false);

        externalAccountsList.add(externalAccountsBehance);
        externalAccountsList.add(externalAccountsBitBucket);
        externalAccountsList.add(externalAccountsDribbble);
        externalAccountsList.add(externalAccountsGithub);
        externalAccountsList.add(externalAccountsLinkedIn);
        externalAccountsList.add(externalAccountsStackOverflow);
        externalAccountsList.add(externalAccountsTwitter);
        return externalAccountsList;
    }

    private MemberProfile buildMemberProfile() {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle(handle);
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());
        return memberProfile;
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
    public void init() throws SupplyException {

        ExternalAccountsBehance externalAccountsBehance = buildExternalAccountsBehance();
        ExternalAccountsBitBucket externalAccountsBitBucket = buildExternalAccountsBitBucket();
        ExternalAccountsDribbble externalAccountsDribbble = buildExternalAccountsDribbble();
        ExternalAccountsGithub externalAccountsGithub = buildExternalAccountsGithub();
        ExternalAccountsLinkedIn externalAccountsLinkedIn = buildExternalAccountsLinkedIn();
        ExternalAccountsStackOverflow externalAccountsStackOverflow = buildExternalAccountsStackOverflow();
        ExternalAccountsTwitter externalAccountsTwitter = buildExternalAccountsTwitter();
        MemberProfile memberProfile = buildMemberProfile();
        List<ExternalAccounts> externalAccountsList = buildExternalAccounts();

        when(memberProfileDAO.validateHandle(anyString(), anyObject(), anyBoolean())).thenReturn(memberProfile);
        when(memberExternalAccountsDAO.getExternalAccounts(userId.toString())).thenReturn(externalAccountsList);
        when(memberExternalAccountsDAO.getMemberExternalAccountsBehance(userId)).thenReturn(externalAccountsBehance);
        when(memberExternalAccountsDAO.getMemberExternalAccountsBitBucket(userId)).thenReturn(externalAccountsBitBucket);
        when(memberExternalAccountsDAO.getMemberExternalAccountsDribbble(userId)).thenReturn(externalAccountsDribbble);
        when(memberExternalAccountsDAO.getMemberExternalAccountsGithub(userId)).thenReturn(externalAccountsGithub);
        when(memberExternalAccountsDAO.getMemberExternalAccountsLinkedIn(userId)).thenReturn(externalAccountsLinkedIn);
        when(memberExternalAccountsDAO.getMemberExternalAccountsStackOverflow(userId)).thenReturn(externalAccountsStackOverflow);
        when(memberExternalAccountsDAO.getMemberExternalAccountsTwitter(userId)).thenReturn(externalAccountsTwitter);
    }

    @Test
    public void testGetMemberExternalAccounts() throws SupplyException, InterruptedException {

        MemberExternalAccounts memberExternalAccounts = memberExternalAccountsManager.getMemberExternalAccounts(handle);

        verify(memberProfileDAO).validateHandle(anyString(), anyObject(), anyBoolean());
        verify(memberExternalAccountsDAO).getExternalAccounts(anyString());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsBehance(anyInt());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsBitBucket(anyInt());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsDribbble(anyInt());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsGithub(anyInt());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsLinkedIn(anyInt());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsStackOverflow(anyInt());
        verify(memberExternalAccountsDAO).getMemberExternalAccountsTwitter(anyInt());

        assertNotNull(memberExternalAccounts);
        assertEquals(memberExternalAccounts.getUserId(), userId);
        assertEquals(memberExternalAccounts.getHandle(), handle);

        assertEquals(memberExternalAccounts.getBehance().getName(), "Faydi");
        assertEquals(memberExternalAccounts.getBehance().getSummary(), "designed");
        assertEquals(memberExternalAccounts.getBehance().getProfileUrl(), profileURL);
        assertEquals(memberExternalAccounts.getBehance().getProjectAppreciations(), Long.valueOf(5393));

        assertEquals(memberExternalAccounts.getBitbucket().getFollowers(), Long.valueOf(0));
        assertEquals(memberExternalAccounts.getBitbucket().getLanguages(), "html/css");
        assertEquals(memberExternalAccounts.getBitbucket().getProfileUrl(), profileURL);
        assertEquals(memberExternalAccounts.getBitbucket().getRepos(), Long.valueOf(1));

        assertEquals(memberExternalAccounts.getDribbble().getHandle(), "simplebits");
        assertEquals(memberExternalAccounts.getDribbble().getFollowers(), Long.valueOf(0));
        assertEquals(memberExternalAccounts.getDribbble().getName(), "Dan");
        assertEquals(memberExternalAccounts.getDribbble().getLikes(), Long.valueOf(39728));
        assertEquals(memberExternalAccounts.getDribbble().getProfileUrl(), profileURL);

        assertEquals(memberExternalAccounts.getGithub().getFollowers(), Long.valueOf(0));
        assertEquals(memberExternalAccounts.getGithub().getHandle(), handle);
        assertEquals(memberExternalAccounts.getGithub().getProfileUrl(), profileURL);
        assertEquals(memberExternalAccounts.getGithub().getPublicRepos(), Long.valueOf(1));
        assertEquals(memberExternalAccounts.getGithub().getLanguages(), "Java");

        assertEquals(memberExternalAccounts.getLinkedin().getHandle(), handle);
        assertEquals(memberExternalAccounts.getLinkedin().getSocialId(), "100");
        assertEquals(memberExternalAccounts.getLinkedin().getSummary(), "Engineer");
        assertEquals(memberExternalAccounts.getLinkedin().getTitle(), "Principal Engineer");
        assertEquals(memberExternalAccounts.getLinkedin().getProfileUrl(), profileURL);

        assertEquals(memberExternalAccounts.getStackoverflow().getName(), handle);
        assertEquals(memberExternalAccounts.getStackoverflow().getSocialId(), "365172");
        assertEquals(memberExternalAccounts.getStackoverflow().getAnswers(), Long.valueOf(42));
        assertEquals(memberExternalAccounts.getStackoverflow().getQuestions(), Long.valueOf(9));
        assertEquals(memberExternalAccounts.getStackoverflow().getReputation(), Long.valueOf(938L));
        assertEquals(memberExternalAccounts.getStackoverflow().getProfileUrl(), profileURL);

        assertEquals(memberExternalAccounts.getTwitter().getHandle(), handle);
        assertEquals(memberExternalAccounts.getTwitter().getSocialId(), "100");
        assertEquals(memberExternalAccounts.getTwitter().getBio(), "Engineer");
        assertEquals(memberExternalAccounts.getTwitter().getNoOfTweets(), Long.valueOf(263));
        assertEquals(memberExternalAccounts.getTwitter().getProfileUrl(), profileURL);
    }
}
