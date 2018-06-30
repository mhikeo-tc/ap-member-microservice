package com.appirio.service.member.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.appirio.service.member.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents DAO for Member external accounts
 *
 * Created by rakeshrecharla on 9/10/15.
 */
public class MemberExternalAccountsDAO {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberExternalAccountsDAO.class);

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Constructor to initialize dynamodb mapper
     * @param mapper    dynamodb mapper
     */
    public MemberExternalAccountsDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public List<ExternalAccounts> getExternalAccounts(String userId) {

        ExternalAccounts externalAccounts = new ExternalAccounts();
        externalAccounts.setUserId(userId);

        DynamoDBQueryExpression<ExternalAccounts> query = new DynamoDBQueryExpression<ExternalAccounts>()
                .withHashKeyValues(externalAccounts).withConsistentRead(false);

        List<ExternalAccounts> results = mapper.query(ExternalAccounts.class, query);
        return results;
    }

    /**
     * Get member's behance external accounts
     * @param userId                        Id of the user
     * @return ExternalAccountsBehance      External accounts behance
     */
    public ExternalAccountsBehance getMemberExternalAccountsBehance(Integer userId) {

        return mapper.load(ExternalAccountsBehance.class, userId);
    }

    /**
     * Get member's bitbucket external accounts
     * @param userId                        Id of the user
     * @return ExternalAccountsBitBucket    External accounts bit bucket
     */
    public ExternalAccountsBitBucket getMemberExternalAccountsBitBucket(Integer userId)  {

        return mapper.load(ExternalAccountsBitBucket.class, userId);
    }

    /**
     * Get member external accounts dribbble
     * @param userId                        Id of the user
     * @return ExternalAccountsDribbble     Externat accounts dribbble
     */
    public ExternalAccountsDribbble getMemberExternalAccountsDribbble(Integer userId)  {

        return mapper.load(ExternalAccountsDribbble.class, userId);
    }

    /**
     * Get member external accounts github
     * @param userId                    Id of the user
     * @return ExternalAccountsGithub   External accounts github
     */
    public ExternalAccountsGithub getMemberExternalAccountsGithub(Integer userId)  {

        return mapper.load(ExternalAccountsGithub.class, userId);
    }

    /**
     * Get member external accounts linkedIn
     * @param userId                        Id of the user
     * @return ExternalAccountsLinkedIn     External accounts linkedIn
     */
    public ExternalAccountsLinkedIn getMemberExternalAccountsLinkedIn(Integer userId)  {

        return mapper.load(ExternalAccountsLinkedIn.class, userId);
    }

    /**
     * Get member external accounts stack overflow
     * @param userId                            Id of the user
     * @return ExternalAccountsStackOverflow    External accounts stack overflow
     */
    public ExternalAccountsStackOverflow getMemberExternalAccountsStackOverflow(Integer userId)  {

        return mapper.load(ExternalAccountsStackOverflow.class, userId);
    }

    /**
     * Get member external accounts twitter
     * @param userId                        Id of the user
     * @return ExternalAccountsTwitter      External accounts twitter
     */
    public ExternalAccountsTwitter getMemberExternalAccountsTwitter(Integer userId) {

        return mapper.load(ExternalAccountsTwitter.class, userId);
    }
}