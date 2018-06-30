package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appirio.service.member.api.MemberDistributionStats;

/**
 * Represents DAO for Member distribution stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class MemberDistributionStatsDAO {

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Constructor to initialize mapper
     * @param mapper    dynamodb mapper
     */
    public MemberDistributionStatsDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Get member distribution stats
     * @param track                         Track
     * @param subTrack                      Sub track
     * @return  MemberDistributionStats     Member distribution stats
     */
    public MemberDistributionStats getMemberDistributionStats(String track, String subTrack) {

        MemberDistributionStats memberDistributionStats = mapper.load(MemberDistributionStats.class, track, subTrack);
        return memberDistributionStats;
    }
}