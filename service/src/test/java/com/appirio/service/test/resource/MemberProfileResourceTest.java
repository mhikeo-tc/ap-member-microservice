package com.appirio.service.test.resource;


import com.appirio.service.member.api.*;
import com.appirio.service.member.manager.MemberProfileManager;
import com.appirio.service.member.resources.MemberProfileResource;
import com.appirio.service.test.BaseTest;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import com.appirio.tech.core.api.v3.request.PostPutRequest;
import com.appirio.tech.core.api.v3.response.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import javax.ws.rs.core.SecurityContext;

import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rakeshrecharla on 8/12/15.
 */
public class MemberProfileResourceTest extends BaseTest {

    private static final MemberProfileManager memberProfileManager = PowerMockito.mock(MemberProfileManager.class);

    private static final Integer userId = 151743;

    private static final String handle = "Ghostar";

    private static final String domain = "api.topcoder-dev.com";

    private static final Long token = 12345L;

    private static final String photoURL =  "https://" + domain.split("\\.")[1] + "-media.s3.amazonaws.com/member/profile/" +
            handle + "-" + token + ".jpg";

    private static final MemberProfileResource memberProfileResource = new MemberProfileResource(memberProfileManager);

    private MemberProfile buildMemberProfile() {

        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(userId);
        memberProfile.setHandle(handle);
        memberProfile.setEmail("email@domain.com");
        memberProfile.setStatus(MemberStatus.ACTIVE.toString());

        memberProfile.setPhotoURL(photoURL);
        return memberProfile;
    }

    @Before
    public void init() throws Exception {

        MemberProfile memberProfile = buildMemberProfile();
        memberProfile.setUserId(userId);
        when(memberProfileManager.getMemberProfile(anyString(), anyObject())).thenReturn(memberProfile);
        when(memberProfileManager.updateMemberProfile(anyString(), anyObject(), anyObject())).thenReturn(memberProfile);
        when(memberProfileManager.updatePhoto(anyString(), anyObject(), anyObject())).thenReturn(photoURL);

        PhotoTokenURL photoTokenURL = new PhotoTokenURL();
        photoTokenURL.setToken(token);
        photoTokenURL.setPreSignedURL(photoURL);
        when(memberProfileManager.generatePhotoUploadUrl(anyString(), anyObject(), anyObject())).thenReturn(photoTokenURL);
    }

    @Test
    public void testGetMemberProfile() throws SupplyException {

        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return createUser(userId.toString());
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
        ApiResponse apiResponse = memberProfileResource.getMemberProfile(handle, null,securityContext);
        verify(memberProfileManager).getMemberProfile(anyString(), anyObject());
        MemberProfile memberProfile = (MemberProfile) apiResponse.getResult().getContent();

        assertNotNull(memberProfile);
        assertEquals(memberProfile.getHandle(), "Ghostar");
        assertEquals(memberProfile.getEmail(), "email@domain.com");
        assertEquals(memberProfile.getPhotoURL(), photoURL);
    }

    @Test
    public void testUpdateMemberProfile() {

        PostPutRequest<MemberProfile> postPutRequest = new PostPutRequest<MemberProfile>();
        ApiResponse apiResponse = memberProfileResource.updateMemberProfile(handle, createUser(userId.toString()), postPutRequest);

        MemberProfile memberProfile = (MemberProfile) apiResponse.getResult().getContent();
        assertNotNull(memberProfile);
        assertEquals(memberProfile.getHandle(), "Ghostar");
        assertEquals(memberProfile.getEmail(), "email@domain.com");
        assertEquals(memberProfile.getPhotoURL(), photoURL);
    }

    @Test
    public void testGeneratePhotoUploadUrl() {

        PostPutRequest<PhotoContentType> postPutRequest = new PostPutRequest<PhotoContentType>();
        ApiResponse apiResponse = memberProfileResource.generatePhotoUploadUrl(handle, createUser(userId.toString()), postPutRequest);
        PhotoTokenURL photoTokenURL = (PhotoTokenURL) apiResponse.getResult().getContent();

        assertNotNull(photoTokenURL);
        assertEquals(photoTokenURL.getPreSignedURL(), photoURL);
        assertEquals(photoTokenURL.getToken(), token);
    }

    @Test
    public void testUpdatePhoto() {

        PhotoTokenContentType photoTokenContentType = new PhotoTokenContentType();
        photoTokenContentType.setToken(token);

        PostPutRequest<PhotoTokenContentType> putRequest = new PostPutRequest<PhotoTokenContentType>();
        putRequest.setParam(photoTokenContentType);
        ApiResponse apiResponse = memberProfileResource.updatePhoto(handle, createUser(userId.toString()), putRequest);

        String content = (String) apiResponse.getResult().getContent();
        assertEquals(content, photoURL);
    }

}