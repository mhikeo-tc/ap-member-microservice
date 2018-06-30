package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts twitter
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.Twitter")
public class ExternalAccountsTwitter {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    @JsonIgnore
    private Integer userId;

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
     * Bio
     */
    @Getter
    @Setter
    private String bio;

    /**
     * No of tweets
     */
    @Getter
    @Setter
    private Long noOfTweets;

    /**
     * Profile URL
     */
    @JsonProperty("profileURL")
    @Getter
    @Setter
    private String profileUrl;

    /**
     * Profile image
     */
    @Getter
    @Setter
    private String profileImage;
}