package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member external link data
 *
 * Created by rakeshrecharla on 10/19/15.
 */
@DynamoDBTable(tableName="Externals.LinksData")
@AllArgsConstructor
@NoArgsConstructor
public class MemberExternalLinkData implements RESTResource {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    private Integer userId;

    /**
     * External link key
     */
    @DynamoDBRangeKey
    @Getter
    @Setter
    private String key;

    /**
     * Handle
     */
    @Getter
    @Setter
    private String handle;

    /**
     * Description
     */
    @Getter
    @Setter
    private String description;

    /**
     * Entities
     */
    @Getter
    @Setter
    private String entities;

    /**
     * Keywords
     */
    @Getter
    @Setter
    private String keywords;

    /**
     * Title
     */
    @Getter
    @Setter
    private String title;

    /**
     * Images
     */
    @Getter
    @Setter
    private String images;

    /**
     * Source
     */
    @Getter
    @Setter
    private String source;

    /**
     * Synchronized at date
     */
    @Getter
    @Setter
    private Long synchronizedAt;

    /**
     * External link URL
     */
    @JsonProperty("URL")
    private String url;

    @JsonProperty("URL")
    public String getUrl() {
        return url;
    }

    @JsonProperty("URL")
    public void setUrl(String url) {
        this.url = url;
    }
}