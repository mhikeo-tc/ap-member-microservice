package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents External accounts
 *
 * Created by rakeshrecharla on 10/6/15.
 */
@DynamoDBTable(tableName="Externals.Accounts")
public class ExternalAccounts {

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    private String userId;

    /**
     * Id of the user
     */
    @DynamoDBRangeKey
    @Getter
    @Setter
    private String accountType;

    /**
     * Flag to specify deleted or not
     */
    @Getter
    @Setter
    private Boolean isDeleted;
}