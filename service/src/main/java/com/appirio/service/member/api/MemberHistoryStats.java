package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.appirio.service.member.marshaller.DataScienceHistoryTrackMarshaller;
import com.appirio.service.member.marshaller.DevelopHistoryTrackMarshaller;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member history stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
@DynamoDBTable(tableName="MemberStatsHistory")
@AllArgsConstructor
@NoArgsConstructor
public class MemberHistoryStats extends BaseModel {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    private Integer userId;

    /**
     * Handle of the user
     */
    @Getter
    @Setter
    private String handle;

    /**
     * Handle Lower
     */
    @DynamoDBIndexHashKey(globalSecondaryIndexName="handleLower-index", attributeName="handleLower")
    @JsonIgnore
    @Getter
    @Setter
    private String handleLower;

    /**
     * Development stats
     */
    @DynamoDBMarshalling(marshallerClass = DevelopHistoryTrackMarshaller.class)
    @JsonProperty("DEVELOP")
    @DynamoDBAttribute(attributeName = "DEVELOP")
    private DevelopHistoryTrack develop;

    @JsonProperty("DEVELOP")
    public DevelopHistoryTrack getDevelop() {
        return develop;
    }

    @JsonProperty("DEVELOP")
    public void setDevelop(DevelopHistoryTrack develop) {
        this.develop = develop;
    }

    /**
     * Data science stats
     */
    @DynamoDBMarshalling(marshallerClass = DataScienceHistoryTrackMarshaller.class)
    @JsonProperty("DATA_SCIENCE")
    @DynamoDBAttribute(attributeName = "DATA_SCIENCE")
    private DataScienceHistoryTrack dataScience;

    @JsonProperty("DATA_SCIENCE")
    public DataScienceHistoryTrack getDataScience() {
        return dataScience;
    }

    @JsonProperty("DATA_SCIENCE")
    public void setDataScience(DataScienceHistoryTrack dataScience) {
        this.dataScience = dataScience;
    }
}