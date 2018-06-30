package com.appirio.service.member.dao.validation;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.marshaller.MemberProfileSettings;
import com.appirio.supply.Messages;
import com.appirio.supply.constants.Track;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;
import com.appirio.service.member.util.Constants;
import com.neovisionaries.i18n.CountryCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the MemberProfileValidator used to validate the user profile data
 * 
 * <p>
 * Changes in the version 1.1 (Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0)
 * - remove the email validation
 * </p>
 * 
 * @author TCCoder
 * @version 1.1
 *
 */
public class MemberProfileValidator implements CustomValidator<MemberProfile> {

    /**
     * Maximum name length
     */
    private Integer maxNameLength = MemberProfileSettings.maxNameLength;

    /**
     * Message result
     */
    List<String> result = new ArrayList<String>();

    /**
     * Member profile DAO
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * Default constructor for initializes member profile dao
     */
    public MemberProfileValidator() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
       
        if(Constants.DYNAMODB_URL != null) {
            client.withEndpoint(Constants.DYNAMODB_URL);
        }

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        memberProfileDAO = new MemberProfileDAO(mapper);
    }


    /**
     * Validate name
     * @param name Name
     */
    private void validateName(String name) {

        if (name == null) {
            return;
        }

        if (name.length() > maxNameLength) {
            result.add(String.format(Messages.NAME_LENGTH_EXCEEDED, maxNameLength));
        }
    }

    /**
     * Validate handle
     * @param existingHandle    Existing handle
     * @param handle            Handle
     */
    private void validateHandle(String existingHandle, String handle) {

        if (handle == null) {
            return;
        }

        if (!handle.equals(existingHandle)) {
            result.add(String.format(Messages.HANDLE_CANNOT_BE_MODIFIED, existingHandle));
        }
    }

    /**
     * Validate country code
     * @param countryCode      Country code
     */
    private String validateCountryCode(String countryCode) {

        if (countryCode == null) {
            return null;
        }

        CountryCode code = CountryCode.getByCodeIgnoreCase(countryCode);
        if (code == null) {
            result.add(String.format(Messages.COUNTRY_CODE_NOT_VALID, countryCode));
            return null;
        }

        String codeAlpha3 = code.getAlpha3();
        if (!countryCode.equalsIgnoreCase(codeAlpha3)) {
            result.add(String.format(Messages.COUNTRY_CODE_NOT_ALPHA3, countryCode));
        }

        return countryCode.toUpperCase();
    }

    /**
     * Validate photo URL
     * @param existingPhotoURL      Existing photo URL
     * @param photoURL              Photo URL
     */
    private void validatePhotoURL(String existingPhotoURL, String photoURL) {

        if (photoURL == null) {
            return;
        }

        if (!photoURL.equals(existingPhotoURL)) {
            result.add(String.format(Messages.PHOTO_URL_CANNOT_BE_MODIFIED, existingPhotoURL));
        }
    }

    /**
     * Validate track type
     * @param tracks    List of tracks
     */
    private List<String> validateTracks(List<String> tracks) {

        if (tracks == null || tracks.isEmpty()) {
            return null;
        }

        Collections.sort(tracks);
        for(int i=0; i < tracks.size(); i++) {
            tracks.set(i, tracks.get(i).toUpperCase());
        }

        for (String track : tracks) {
            if (!Track.isValid(track)) {
                result.add(String.format(Messages.TRACKS_NOT_VALID, tracks.toString()));
            }
        }

        return tracks;
    }


    /**
     * Validate Member profile
     * @param memberProfile     Member profile
     * @return List<String>     List of validation messages
     */
    public List<String> validate(MemberProfile memberProfile) {

        MemberProfile existingMemberProfile = memberProfileDAO.getMemberProfile(memberProfile.getHandleLower());

        // Validate firstName
        validateName(memberProfile.getFirstName());

        // Validate lastName
        validateName(memberProfile.getLastName());

        // Validate otherLangName
        validateName(memberProfile.getOtherLangName());

        // Validate handle
        validateHandle(existingMemberProfile.getHandle(), memberProfile.getHandle());

        // Validate homeCountryCode
        memberProfile.setHomeCountryCode(validateCountryCode(memberProfile.getHomeCountryCode()));

        // Validate competitionCountryCode
        memberProfile.setCompetitionCountryCode(validateCountryCode(memberProfile.getCompetitionCountryCode()));

        // Validate photoURL
        validatePhotoURL(existingMemberProfile.getPhotoURL(), memberProfile.getPhotoURL());

        // Validate tracks
        memberProfile.setTracks(validateTracks(memberProfile.getTracks()));

        return result;
    }
}
