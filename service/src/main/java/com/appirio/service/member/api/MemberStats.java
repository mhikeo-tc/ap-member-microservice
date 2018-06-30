package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.appirio.service.member.marshaller.CopilotStatsMarshaller;
import com.appirio.service.member.marshaller.DataScienceStatsMarshaller;
import com.appirio.service.member.marshaller.DesignStatsMarshaller;
import com.appirio.service.member.marshaller.DevelopStatsMarshaller;
import com.appirio.service.member.marshaller.MaxRatingMarshaller;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member stats
 *
 * Created by rakeshrecharla on 7/9/15.
 */
@DynamoDBTable(tableName="MemberStats")
@AllArgsConstructor
@NoArgsConstructor
public class MemberStats extends BaseModel {

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
     * User highest rating information
     */
    @Getter
    @Setter
    @DynamoDBMarshalling(marshallerClass = MaxRatingMarshaller.class)
    private MaxRating maxRating;

    /**
     * Number of challenges
     */
    @Getter
    @Setter
    private Long challenges;

    /**
     * Number of wins
     */
    @Getter
    @Setter
    private Long wins;

    /**
     * Development stats
     */
    @DynamoDBMarshalling(marshallerClass = DevelopStatsMarshaller.class)
    @DynamoDBAttribute(attributeName = "DEVELOP")
    @JsonProperty("DEVELOP")
    private DevelopStats develop;

    @JsonProperty("DEVELOP")
    public DevelopStats getDevelop() {
        return develop;
    }

    @JsonProperty("DEVELOP")
    public void setDevelop(DevelopStats develop) {
        this.develop = develop;
    }

    /**
     * Design stats
     */
    @DynamoDBMarshalling(marshallerClass = DesignStatsMarshaller.class)
    @JsonProperty("DESIGN")
    @DynamoDBAttribute(attributeName = "DESIGN")
    private DesignStats design;

    @JsonProperty("DESIGN")
    public DesignStats getDesign() {
        return design;
    }

    @JsonProperty("DESIGN")
    public void setDesign(DesignStats design) {
        this.design = design;
    }

    /**
     * Data science stats
     */
    @DynamoDBMarshalling(marshallerClass = DataScienceStatsMarshaller.class)
    @JsonProperty("DATA_SCIENCE")
    @DynamoDBAttribute(attributeName = "DATA_SCIENCE")
    private DataScienceStats dataScience;

    @JsonProperty("DATA_SCIENCE")
    public DataScienceStats getDataScience() {
        return dataScience;
    }

    @JsonProperty("DATA_SCIENCE")
    public void setDataScience(DataScienceStats dataScience) {
        this.dataScience = dataScience;
    }

    /**
     * Copilot stats
     */
    @DynamoDBMarshalling(marshallerClass = CopilotStatsMarshaller.class)
    @JsonProperty("COPILOT")
    @DynamoDBAttribute(attributeName = "COPILOT")
    private CopilotStats copilot;

    @JsonProperty("COPILOT")
    public CopilotStats getCopilot() {
        return copilot;
    }

    @JsonProperty("COPILOT")
    public void setCopilot(CopilotStats copilot) {
        this.copilot = copilot;
    }
}
