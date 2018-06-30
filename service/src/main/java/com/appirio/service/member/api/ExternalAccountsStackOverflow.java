package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts stack overflow
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.Stackoverflow")
public class ExternalAccountsStackOverflow {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    @JsonIgnore
    private Integer userId;

    /**
     * Name
     */
    @Getter
    @Setter
    private String name;

    /**
     * Social id
     */
    @Getter
    @Setter
    private String socialId;

    /**
     * Answers
     */
    @Getter
    @Setter
    private Long answers;

    /**
     * Profile URL
     */
    @JsonProperty("profileURL")
    @Getter
    @Setter
    private String profileUrl;

    /**
     * Questions
     */
    @Getter
    @Setter
    private Long questions;

    /**
     * Reputation
     */
    @Getter
    @Setter
    private Long reputation;

    /**
     * Top tags
     */
    @Getter
    @Setter
    private String topTags;
}