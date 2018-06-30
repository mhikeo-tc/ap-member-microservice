package com.appirio.service.test.dao.util;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DynamoDb utility for table creation, load, deletion
 *
 * Created by rakeshrecharla on 9/13/15.
 */
public class DynamoDbTableUtil {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(DynamoDbTableUtil.class);

    /**
     * Amazon dynamodb
     */
    private AmazonDynamoDB amazonDynamoDB;

    /**
     * Dynamodb
     */
    private DynamoDB dynamoDB;

    /**
     * Dynamodb mapper
     */
    @Getter
    @Setter
    private DynamoDBMapper mapper;

    /**
     * Constructot to initialize db
     */
    public DynamoDbTableUtil() {
        this(null);
    }

    /**
     * Constructot to initialize db
     */
    public DynamoDbTableUtil(String dynamoDBUrl) {
        this.amazonDynamoDB = new AmazonDynamoDBClient(new ProfileCredentialsProvider());

        if(dynamoDBUrl != null) {
            ((AmazonDynamoDBClient)amazonDynamoDB).withEndpoint(dynamoDBUrl);
        }

        this.dynamoDB = new DynamoDB(amazonDynamoDB);
        amazonDynamoDB.setEndpoint("http://localhost:8000");
        this.mapper = new DynamoDBMapper(amazonDynamoDB);
    }

    /**
     * Create dynamodb table
     * @param tableName            Table name
     * @param keySchema            Key Schema
     * @param attributeDefinitions Attribute definitions
     * @param gsi                  Global secondary index
     */
    public void createDynamoDbTable(String tableName, List<KeySchemaElement> keySchema,
                                    List<AttributeDefinition> attributeDefinitions, GlobalSecondaryIndex gsi) {

        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withAttributeDefinitions(attributeDefinitions)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(1L)
                            .withWriteCapacityUnits(1L));

            if (gsi != null) {
                request = request.withGlobalSecondaryIndexes(gsi);
            }

            logger.debug("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);

            logger.debug("Waiting for " + tableName
                    + " to be created...this may take a while...");
            table.waitForActive();

        } catch (Exception e) {
            logger.error("CreateTable request failed for " + tableName);
            logger.error(e.getMessage());
        }
    }

    /**
     * Get Dynamodb table information
     * @param tableName Table name
     */
    public void getDynamoDBTableInformation(String tableName) {

        logger.debug("Describing " + tableName);
        Table table = dynamoDB.getTable(tableName);

        TableDescription tableDescription = table.describe();
        logger.debug(String.format("Name: %s:\n" + "Status: %s \n"
                        + "Provisioned Throughput (read capacity units/sec): %d \n"
                        + "Provisioned Throughput (write capacity units/sec): %d \n",

                tableDescription.getTableName(),
                tableDescription.getTableStatus(),
                tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
                tableDescription.getProvisionedThroughput().getWriteCapacityUnits()));
    }

    /**
     * Load dynamodb table
     * @param tableName Table name
     * @param item      Item
     */
    public void loadDynamoDbTable(String tableName, Item item) {

        Table table = dynamoDB.getTable(tableName);
        logger.debug("Adding data to " + tableName);

        try {
            table.putItem(item);
        } catch (Exception e) {
            logger.error("Failed to create items in " + tableName);
            logger.error(e.getMessage());
        }
    }

    /**
     * Delete dynamodb table
     * @param tableName table name
     */
    public void deleteDynamoDbTable(String tableName) {

        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();

            System.out.println("Waiting for " + tableName
                    + " to be deleted...this may take a while...");

            table.waitForDelete();
        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

}
