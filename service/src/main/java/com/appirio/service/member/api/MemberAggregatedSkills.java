package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.appirio.service.member.marshaller.MemberSkillsMarshaller;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Represents Member aggregated skills
 *
 * Created by rakeshrecharla on 8/31/15.
 */
@DynamoDBTable(tableName="MemberAggregatedSkills")
@AllArgsConstructor
@NoArgsConstructor
public class MemberAggregatedSkills extends BaseModel {

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
    @DynamoDBMarshalling(marshallerClass = MemberSkillsMarshaller.class)
    @Getter
    @Setter
    private Map<Long, MemberSkills> skills;
}