package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.appirio.service.member.api.MemberExternalLink;
import com.appirio.service.member.api.MemberExternalLinkData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents DAO for Member external links
 *
 * Created by rakeshrecharla on 10/19/15.
 */
public class MemberExternalLinksDAO {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalAccountsDAO.class);

    /**
     * DynamoDB
     */
    private DynamoDB dynamoDB;

    /**
     * Dynamodb mapper
     */
    private DynamoDBMapper mapper;

    /**
     * Externals Links table
     */
    private static final String ExternalLinksTable = "Externals.Links";

    /**
     * Constructor to initialize dynamodb mapper
     * @param mapper    dynamodb mapper
     * @param dynamoDB  dynamodb
     */
    public MemberExternalLinksDAO(DynamoDBMapper mapper, DynamoDB dynamoDB) {
        this.mapper = mapper;
        this.dynamoDB = dynamoDB;
    }

    /**
     * Get member external links
     * @param userId    Id of the user
     * @return          Member external link list
     */
    public List<MemberExternalLink> getMemberExternalLinks(Integer userId) {

        MemberExternalLink memberExternalLink = new MemberExternalLink();
        memberExternalLink.setUserId(userId);

        DynamoDBQueryExpression<MemberExternalLink> query = new DynamoDBQueryExpression<MemberExternalLink>()
                .withHashKeyValues(memberExternalLink).withConsistentRead(false);

        List<MemberExternalLink> results = mapper.query(MemberExternalLink.class, query);
        return results;
    }

    /**
     * Get member external links data
     * @param userId    Id of the user
     * @return          Member external link data list
     */
    public List<MemberExternalLinkData> getMemberExternalLinksData(Integer userId) {

        MemberExternalLinkData memberExternalLinkData = new MemberExternalLinkData();
        memberExternalLinkData.setUserId(userId);

        DynamoDBQueryExpression<MemberExternalLinkData> query = new DynamoDBQueryExpression<MemberExternalLinkData>()
                .withHashKeyValues(memberExternalLinkData).withConsistentRead(false);

        List<MemberExternalLinkData> results = mapper.query(MemberExternalLinkData.class, query);
        return results;
    }

    /**
     * Get member external link
     * @param userId    Id of the user
     * @param key       key
     * @return          Member external link
     */
    public MemberExternalLink getMemberExternalLink(Integer userId, String key) {
        return mapper.load(MemberExternalLink.class, userId, key);
    }

    /**
     * Update member external link
     * @param memberExternalLink    Member external link
     */
    public void updateMemberExternalLink(MemberExternalLink memberExternalLink) {

        Table table = dynamoDB.getTable(ExternalLinksTable);
        Item item = new Item()
                .withPrimaryKey("userId", memberExternalLink.getUserId(), "key", memberExternalLink.getKey())
                .withString("URL", memberExternalLink.getUrl())
                .withBoolean("hasErrored", memberExternalLink.getHasErrored())
                .withBoolean("isDeleted", memberExternalLink.getIsDeleted())
                .withLong("synchronizedAt", memberExternalLink.getSynchronizedAt())
                .withLong("createdAt", memberExternalLink.getCreatedAtDD())
                .withLong("updatedAt", memberExternalLink.getUpdatedAtDD());
        table.putItem(item);
    }
}