package com.appirio.service.member.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.appirio.service.member.api.MemberProfileTrait;
import com.appirio.supply.SupplyException;
import com.appirio.supply.dataaccess.queryhandler.handler.ValidationHandler;

/**
 * MemberProfileTraitsDAO is used to manage the member profile trait data in the
 * database
 *
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 *
 * @author TCCoder
 * @version 1.0
 *
 */
public class MemberProfileTraitsDAO {

    /**
     * Dynamodb mapper
     */
    private final DynamoDBMapper mapper;

    /**
     * Create MemberProfileTraitsDAO
     *
     * @param mapper the mapper to use
     */
    public MemberProfileTraitsDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Get member profile
     *
     * @param userId the userId to use
     * @return the List<MemberProfileTrait> result
     */
    public List<MemberProfileTrait> getMemberProfile(Long userId) throws SupplyException {
        MemberProfileTrait trait = new MemberProfileTrait();
        trait.setUserId(userId);
        DynamoDBQueryExpression<MemberProfileTrait> query = new DynamoDBQueryExpression<MemberProfileTrait>().withHashKeyValues(trait)
                .withConsistentRead(false);
        List<MemberProfileTrait> results = mapper.query(MemberProfileTrait.class, query);

        results.forEach(item -> {
            item.setUserId(null);
            item.getTraits().setTraitId(null);
        });
        return results;
    }

    /**
     * Create member profile trait
     *
     * @param userId the userId to use
     * @param memberProfileTraits the memberProfileTraits to use
     * @param operatorId the operatorId to use
     * @throws SupplyException if any error occurs
     * @return the List<MemberProfileTrait> result
     */
    public List<MemberProfileTrait> createMemberProfileTrait(Long userId, List<MemberProfileTrait> memberProfileTraits, Long operatorId)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {
        if (memberProfileTraits == null || memberProfileTraits.isEmpty()) {
            throw new SupplyException("there should be at least one member profile trait", HttpServletResponse.SC_BAD_REQUEST);
        }
        for (MemberProfileTrait trait : memberProfileTraits) {
            List<String> message = trait.validate();
            ValidationHandler.validationException(message);
        }
        List<String> existingTraits = new ArrayList<String>();
        List<MemberProfileTrait> traits = this.getMemberProfile(userId);
        for (MemberProfileTrait trait : memberProfileTraits) {
            trait.getTraits().setTraitId(trait.getTraitId());
            trait.setUserId(userId);
            for (MemberProfileTrait old : traits) {
                if (trait.getTraitId().equals(old.getTraitId())) {
                    existingTraits.add(old.getTraitId());
                }
            }
        }
        if (existingTraits.size() > 0) {
            throw new SupplyException("The following traits already exist:" + existingTraits, HttpServletResponse.SC_BAD_REQUEST);
        }

        for (MemberProfileTrait trait : memberProfileTraits) {
            trait.setCreatedAt(new Date());
            trait.setCreatedBy(operatorId);
            trait.setUpdatedAt(new Date());
            trait.setUpdatedBy(operatorId);
            mapper.save(trait);
            trait.getTraits().setTraitId(null);
        }

        return memberProfileTraits;
    }

    /**
     * Update member profile trait
     *
     * @param userId the userId to use
     * @param memberProfileTraits the memberProfileTraits to use
     * @param operatorId the operatorId to use
     * @throws SupplyException if any error occurs
     * @return the List<MemberProfileTrait> result
     */
    public List<MemberProfileTrait> updateMemberProfileTrait(Long userId, List<MemberProfileTrait> memberProfileTraits, Long operatorId)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, SupplyException, NoSuchMethodException {
        if (memberProfileTraits == null || memberProfileTraits.isEmpty()) {
            throw new SupplyException("there should be at least one member profile trait", HttpServletResponse.SC_BAD_REQUEST);
        }
        for (MemberProfileTrait trait : memberProfileTraits) {
            List<String> message = trait.validate();
            ValidationHandler.validationException(message);
        }

        List<String> notExistingTraits = new ArrayList<String>();
        List<MemberProfileTrait> traits = this.getMemberProfile(userId);

        for (MemberProfileTrait trait : memberProfileTraits) {
            trait.setUserId(userId);
            trait.getTraits().setTraitId(trait.getTraitId());
            boolean exists = false;
            for (MemberProfileTrait old : traits) {
                if (trait.getTraitId().equals(old.getTraitId())) {
                    exists = true;
                    trait.setCreatedAt(old.getCreatedAt());
                    trait.setCreatedBy(old.getCreatedBy());
                }
            }
            if (!exists) {
                notExistingTraits.add(trait.getTraitId());
            }
        }
        if (notExistingTraits.size() > 0) {
            throw new SupplyException("These traits do not exist:" + notExistingTraits, HttpServletResponse.SC_NOT_FOUND);
        }

        for (MemberProfileTrait trait : memberProfileTraits) {
            trait.setUpdatedAt(new Date());
            trait.setUpdatedBy(operatorId);
            mapper.save(trait);
            trait.getTraits().setTraitId(null);
        }
        return memberProfileTraits;
    }

    /**
     * Delete member profile trait
     *
     * @param userId the userId to use
     * @param memberProfileTraitIds the memberProfileTraitIds to use
     * @throws SupplyException if any error occurs
     */
    public void deleteMemberProfileTrait(Long userId, List<String> memberProfileTraitIds) throws SupplyException {
        List<MemberProfileTrait> notExistingTraits = new ArrayList<MemberProfileTrait>();
        List<MemberProfileTrait> traits = this.getMemberProfile(userId);
        List<MemberProfileTrait> traitsToDeleted = new ArrayList<MemberProfileTrait>();
        for (String traitId : memberProfileTraitIds) {
            MemberProfileTrait trait = null;
            for (MemberProfileTrait old : traits) {
                if (old.getTraitId().equals(traitId)) {
                    trait = old;
                }
            }
            if (trait != null) {
                traitsToDeleted.add(trait);
            } else {
                notExistingTraits.add(trait);
            }
        }
        if (notExistingTraits.size() > 0) {
            throw new SupplyException("The following traits do not exist:" + notExistingTraits, HttpServletResponse.SC_NOT_FOUND);
        }

        traitsToDeleted = memberProfileTraitIds.size() == 0 ? traits : traitsToDeleted;

        for (MemberProfileTrait trait : traitsToDeleted) {
            trait.setUserId(userId);
            mapper.delete(trait);
        }
    }
}
