package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts bit bucket
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.BitBucket")
public class ExternalAccountsBitBucket {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    @JsonIgnore
    private Integer userId;

    /**
     * Handle of the user
     */
    @Getter
    @Setter
    private String handle;

    /**
     * Followers
     */
    @Getter
    @Setter
    private Long followers;

    /**
     * Languages
     */
    @Getter
    @Setter
    private String languages;

    /**
     * Profile URL
     */
    @JsonProperty("profileURL")
    @Getter
    @Setter
    private String profileUrl;

    /**
     * Repos
     */
    @Getter
    @Setter
    private Long repos;
}