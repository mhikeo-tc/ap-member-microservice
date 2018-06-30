package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.appirio.service.member.dao.validation.MemberSkillsValidator;
import com.appirio.service.member.marshaller.MemberInputSkillsMarshaller;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.appirio.supply.dataaccess.api.validation.Validator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * Represents Member entered skills
 *
 * Created by rakeshrecharla on 7/20/15.
 */
@DynamoDBTable(tableName="MemberEnteredSkills")
@AllArgsConstructor
@NoArgsConstructor
@Validator(MemberSkillsValidator.class)
public class MemberEnteredSkills extends BaseModel {

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
     * Identifier of user who created the record
     */
    @Getter
    @Setter
    private String createdBy;

    /**
     * Identifier of user who performed the last update in this record
     */
    @Getter
    @Setter
    private String updatedBy;

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    private Integer userId;

    /**
     * User handle
     */
    @Getter
    @Setter
    private String userHandle;

    /**
     * Handle Lower
     */
    @DynamoDBIndexHashKey(globalSecondaryIndexName="handleLower-index", attributeName="handleLower")
    @JsonIgnore
    @Getter
    @Setter
    private String handleLower;

    /**
     * Skills
     */
    @DynamoDBMarshalling(marshallerClass = MemberInputSkillsMarshaller.class)
    @Getter
    @Setter
    private Map<Long, MemberInputSkills> skills;
}