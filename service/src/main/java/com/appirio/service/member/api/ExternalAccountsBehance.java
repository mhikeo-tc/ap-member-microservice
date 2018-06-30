package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts behance
 *
 * Created by rakeshrecharla on 9/11/15.
 */
@DynamoDBTable(tableName="Externals.Behance")
public class ExternalAccountsBehance {

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
     * Profile URL
     */
    @JsonProperty("profileURL")
    @Getter
    @Setter
    private String profileUrl;

    /**
     * Project appreciations
     */
    @Getter
    @Setter
    private Long projectAppreciations;

    /**
     * Project views
     */
    @Getter
    @Setter
    private Long projectViews;

    /**
     * Summary
     */
    @Getter
    @Setter
    private String summary;

    /**
     * Creative fields
     */
    @Getter
    @Setter
    private String creativeFields;
}