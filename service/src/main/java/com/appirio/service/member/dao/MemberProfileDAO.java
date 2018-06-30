package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.MemberProfileAddress;
import com.appirio.supply.Messages;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.MemberStatus;
import com.appirio.supply.dataaccess.queryhandler.handler.ValidationHandler;
import com.appirio.tech.core.auth.AuthUser;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents DAO for Member profile
 *
 * Created by rakeshrecharla on 8/7/15.
 */
public class MemberProfileDAO {

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Constructor to initialize dynamodb mapper
     * @param mapper    dynamodb mapper
     */
    public MemberProfileDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Validate handle of the user
     * @param handle                    Handle of the user
     * @param authUser                  Authentication user
     * @param openUser                  true for open user, false otherwise
     * @return MemberProfile            Member profile
     * @throws IllegalAccessException   Illegal access exception
     */
    public MemberProfile validateHandle(String handle, AuthUser authUser, Boolean openUser) throws SupplyException {

        MemberProfile memberProfile = getMemberProfile(handle);
        if (memberProfile == null || memberProfile.getUserId() == null) {
            throw new SupplyException(String.format(Messages.HANDLE_NOT_VALID, handle), HttpServletResponse.SC_NOT_FOUND);
        }

        if (!memberProfile.getStatus().equals(MemberStatus.ACTIVE.toString())) {
            throw new SupplyException(String.format(Messages.HANDLE_NOT_ACTIVE, handle), HttpServletResponse.SC_FORBIDDEN);
        }

        if (!openUser && !memberProfile.getUserId().equals(
                Integer.valueOf(authUser.getUserId().toString()))) {
            throw new SupplyException("UserId " + memberProfile.getUserId() +
                    " must be same as logged in UserId " + Integer.valueOf(authUser.getUserId().toString()),
                    HttpServletResponse.SC_FORBIDDEN);
        }

        return memberProfile;
    }

    /**
     * Get member's profile
     * @param handle         Handle of the user
     * @return MemberProfile Member profile
     */
    public MemberProfile getMemberProfile(String handle) {

        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setHandleLower(handle.toLowerCase());

        DynamoDBQueryExpression<MemberProfile> query = new DynamoDBQueryExpression<MemberProfile>().withIndexName("handleLower-index")
                .withHashKeyValues(memberProfile).withConsistentRead(false);

        List<MemberProfile> results = mapper.query(MemberProfile.class, query);
        if (results == null || results.size() == 0) {
            return null;
        }

        return  results.get(0);
    }

    /**
     * Get member's profile
     * @param userId            Id of the user
     * @return MemberProfile    Member profile
     */
    public MemberProfile getMemberProfile(Integer userId) {
        return mapper.load(MemberProfile.class, userId);
    }

    /**
     * Update member profile
     * @param memberProfile              Member profile
     * @param validate                   true for object validation, false otherwise
     * @throws IllegalAccessException    Illegal access exception
     * @throws InvocationTargetException Invocation target exception
     * @throws InstantiationException    Instantiation exception
     * @throws SupplyException           Supply exception
     * @throws NoSuchMethodException     No such method exception
     */
    public void updateMemberProfile(MemberProfile memberProfile, Boolean validate) throws IllegalAccessException,
            InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {

        if (validate) {
            List<String> validationMessages = memberProfile.validate();

            if (memberProfile.getAddresses() != null) {
                List<String> validationMessagesAddress = new ArrayList<String>();
                for (MemberProfileAddress memberProfileAddress : memberProfile.getAddresses()) {
                    List<String> messages = memberProfileAddress.validate();
                    validationMessagesAddress.addAll(messages);
                }
                validationMessages.addAll(validationMessagesAddress);
            }
            ValidationHandler.validationException(validationMessages);
        }

        mapper.save(memberProfile);
    }
}