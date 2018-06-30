package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Represents Member external Link
 *
 * Created by rakeshrecharla on 10/19/15.
 */
@DynamoDBTable(tableName="Externals.Links")
@AllArgsConstructor
@NoArgsConstructor
public class MemberExternalLink extends BaseModel {

    /**
     * Get createdAt epoch time
     * @return Long
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="createdAt")
    public Long getCreatedAtDD() {
        return getCreatedAt() == null ? null : getCreatedAt().getTime();
    }

    /**
     * Set createdAt epoch time
     * @param value
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="createdAt")
    public void setCreatedAtDD(Long value) {
        setCreatedAt(new Date(value));
    }

    /**
     * Get updatedAt epoch time
     * @return Long
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="updatedAt")
    public Long getUpdatedAtDD() {
        return getUpdatedAt() == null ? null : getUpdatedAt().getTime();
    }

    /**
     * Set updatedAt epoch time
     * @param value
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="updatedAt")
    public void setUpdatedAtDD(Long value) {
        setUpdatedAt(new Date(value));
    }

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
     * External link URL
     */
    @JsonProperty("URL")
    @DynamoDBAttribute(attributeName = "URL")
    private String url;

    @JsonProperty("URL")
    public String getUrl() {
        return url;
    }

    @JsonProperty("URL")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Synchronized at date
     */
    @Getter
    @Setter
    private Long synchronizedAt;

    /**
     * Flag to specify deleted or not
     */
    @Getter
    @Setter
    private Boolean isDeleted;

    /**
     * Flag to indicate whether there are any errors
     */
    @Getter
    @Setter
    private Boolean hasErrored;
}