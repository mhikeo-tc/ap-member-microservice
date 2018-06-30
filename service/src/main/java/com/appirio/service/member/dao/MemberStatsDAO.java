package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.member.api.MemberStats;

/**
 * Represents DAO for Member stats
 *
 * Created by rakeshrecharla on 8/12/15.
 */
public class MemberStatsDAO {

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Constructor to initialize dynamodb mapper
     * @param mapper    dynamodb mapper
     */
    public MemberStatsDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Get member's stats
     * @param userId            Id of the user
     * @return MemberStats      Member stats
     */
    public MemberStats getMemberStats(Integer userId) {

        return mapper.load(MemberStats.class, userId);
    }
}
