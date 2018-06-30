package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts dribbble
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.Dribbble")
public class ExternalAccountsDribbble {

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
     * name
     */
    @Getter
    @Setter
    private String name;

    /**
     * Summary
     */
    @Getter
    @Setter
    private String summary;

    /**
     * Followers
     */
    @Getter
    @Setter
    private Long followers;

    /**
     * Likes
     */
    @Getter
    @Setter
    private Long likes;

    /**
     * Profile URL
     */
    @JsonProperty("profileURL")
    @Getter
    @Setter
    private String profileUrl;

    /**
     * Tags
     */
    @Getter
    @Setter
    private String tags;
}
