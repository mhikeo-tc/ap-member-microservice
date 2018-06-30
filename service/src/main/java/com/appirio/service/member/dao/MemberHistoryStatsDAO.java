package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.member.api.MemberHistoryStats;

/**
 * Represents DAO for Member history stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class MemberHistoryStatsDAO {

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Constructor to initialize the dynamodb mapper
     * @param mapper    dynamodb mapper
     */
    public MemberHistoryStatsDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Member history statistics
     * @param userId    Id of the user
     * @return          Member history stats
     */
    public MemberHistoryStats getMemberHistoryStats(Integer userId) {

        return mapper.load(MemberHistoryStats.class, userId);
    }
}