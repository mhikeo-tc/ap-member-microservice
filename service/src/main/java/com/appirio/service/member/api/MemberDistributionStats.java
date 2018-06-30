package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.appirio.service.member.marshaller.SubTrackDistributionStatsMarshaller;
import com.appirio.supply.dataaccess.api.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member distribution stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
@DynamoDBTable(tableName="RatingsDistribution")
@AllArgsConstructor
@NoArgsConstructor
public class MemberDistributionStats extends BaseModel {

    /**
     * Track
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    private String track;

    /**
     * Sub track
     */
    @DynamoDBRangeKey
    @Getter
    @Setter
    private String subTrack;

    /**
     * Sub track distribution stats
     */
    @DynamoDBMarshalling(marshallerClass = SubTrackDistributionStatsMarshaller.class)
    @Getter
    @Setter
    private SubTrackDistributionStats distribution;
}