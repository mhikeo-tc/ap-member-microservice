package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.member.api.MemberAggregatedSkills;
import com.appirio.service.member.api.MemberEnteredSkills;
import com.appirio.supply.SupplyException;
import com.appirio.supply.dataaccess.queryhandler.handler.ValidationHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Represents DAO for Member skills
 *
 * Created by rakeshrecharla on 8/31/15.
 */
public class MemberSkillsDAO {

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Constructor to initialize dynamodb mapper
     * @param mapper    dynamodb mapper
     */
    public MemberSkillsDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Get member's entered skills
     * @param userId                Id of the user
     * @return MemberEnteredSkills  Member entered skills
     */
    public MemberEnteredSkills getMemberEnteredSkills(Integer userId) {

        return mapper.load(MemberEnteredSkills.class, userId);
    }

    /**
     * Update member skills
     * @param memberEnteredSkills     Member entered skills
     */
    public void updateMemberSkills(MemberEnteredSkills memberEnteredSkills) throws IllegalAccessException,
            InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {

        List<String> validationMessages = memberEnteredSkills.validate();
        ValidationHandler.validationException(validationMessages);

        mapper.save(memberEnteredSkills);
    }

    /**
     * Get member aggregated skills
     * @param userId                        Id of the user
     * @return MemberAggregatedSkills       Member aggregated skills
     */
    public MemberAggregatedSkills getMemberAggregatedSkills(Integer userId) {

        return mapper.load(MemberAggregatedSkills.class, userId);
    }
}