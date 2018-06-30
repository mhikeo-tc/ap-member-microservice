package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts linkedIn
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.LinkedIn")
public class ExternalAccountsLinkedIn {

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
     * Handle
     */
    @Getter
    @Setter
    private String handle;

    /**
     * Social id
     */
    @Getter
    @Setter
    private String socialId;

    /**
     * Profile URL
     */
    @JsonProperty("profileURL")
    @Getter
    @Setter
    private String profileUrl;

    /**
     * Summary
     */
    @Getter
    @Setter
    private String summary;

    /**
     * Title
     */
    @Getter
    @Setter
    private String title;

    /**
     * Skills
     */
    @Getter
    @Setter
    private String skills;
}