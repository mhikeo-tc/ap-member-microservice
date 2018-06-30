package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts github
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.Github")
public class ExternalAccountsGithub {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    @JsonIgnore
    private Integer userId;

    /**
     * Followers
     */
    @Getter
    @Setter
    private Long followers;

    /**
     * Handle
     */
    @Getter
    @Setter
    private String handle;

    /**
     * languages
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
     * Public repositories
     */
    @Getter
    @Setter
    private Long publicRepos;

    /**
     * Social id
     */
    @Getter
    @Setter
    private String socialId;
}