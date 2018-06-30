package com.appirio.service.member.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.appirio.service.member.api.MemberProfileBasicInfo;
import com.appirio.service.member.api.MemberProfileCommunities;
import com.appirio.service.member.api.MemberProfileEducation;
import com.appirio.service.member.api.MemberProfileSkill;
import com.appirio.service.member.api.MemberProfileTrait;
import com.appirio.service.member.api.MemberProfileTraitData;
import com.appirio.service.member.api.MemberProfileWork;
import com.appirio.service.member.api.MemberProfileLanguage;
import com.appirio.service.member.api.MemberProfileHobby;
import com.appirio.service.member.api.MemberProfileOrganization;
import com.appirio.service.member.api.MemberProfileDevice;
import com.appirio.service.member.api.MemberProfileSoftware;
import com.appirio.service.member.api.MemberProfileServiceProvider;
import com.appirio.service.member.api.MemberProfileSubscription;
import com.appirio.service.member.api.MemberProfilePersonalization;
import com.appirio.service.member.dao.validation.MemberProfileTraitValidator;
import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * RESTResourceDeserializer is used to deserialize the RESTResource entity
 *
 * <p>
 *    Version 1.0 - It's added in Topcoder Member Service - Add Additional Traits v1.0
 * </p>
 *
 * Version 1.1:
 * - Added more trait types
 *      - LANGUAGES_TRAIT_TYPE
 *      - HOBBY_TRAIT_TYPE
 *      - ORGANIZATION_TRAIT_TYPE
 *
 * Version 1.2:
 * - Added more trait types
 *      - DEVICE_TRAIT_TYPE
 *      - SOFTWARE_TRAIT_TYPE
 *      - SERVICE_PROVIDER_TRAIT_TYPE
 *      - SUBSCRIPTION_TRAIT_TYPE
 *
 * Version 1.3:
 * - Added more trait type validations:
 *      - PERSONALIZATION_TRAIT_TYPE
 *
 * @author TCCoder - thomaskranitsas
 * @version 1.2
 *
 */
public class RESTResourceDeserializer extends JsonDeserializer<List<RESTResource>> {

    /**
     * Deserialize the data
     *
     * @param jp the jp to use
     * @param context the context to use
     * @throws IOException if any error occurs
     * @throws JsonProcessingException if any error occurs
     * @return the List<RESTResource> result
     */
    @Override
    public List<RESTResource> deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonStreamContext parsingContext = jp.getParsingContext();
        JsonStreamContext parent = parsingContext.getParent();
        String traitId = null;
        if (parent.getCurrentValue() != null && parent.getCurrentValue() instanceof MemberProfileTraitData
                && ((MemberProfileTraitData) parent.getCurrentValue()).getTraitId() != null) {
            traitId = ((MemberProfileTraitData) parent.getCurrentValue()).getTraitId();
        } else if (parent.getParent() != null && parent.getParent().getCurrentValue() instanceof MemberProfileTrait) {
            MemberProfileTrait trait = (MemberProfileTrait) parent.getParent().getCurrentValue();
            traitId = trait.getTraitId();
        }

        RESTResource[] rs = null;
        if (MemberProfileTraitValidator.BASIC_INFO_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileBasicInfo[].class);
        } else if (MemberProfileTraitValidator.EDUCATION_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileEducation[].class);
        } else if (MemberProfileTraitValidator.SKILL_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileSkill[].class);
        } else if (MemberProfileTraitValidator.WORK_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileWork[].class);
        } else if (MemberProfileTraitValidator.COMMUNITIES_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileCommunities[].class);
        } else if (MemberProfileTraitValidator.LANGUAGES_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileLanguage[].class);
        } else if (MemberProfileTraitValidator.HOBBY_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileHobby[].class);
        } else if (MemberProfileTraitValidator.ORGANIZATION_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileOrganization[].class);
        } else if (MemberProfileTraitValidator.DEVICE_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileDevice[].class);
        } else if (MemberProfileTraitValidator.SOFTWARE_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileSoftware[].class);
        } else if (MemberProfileTraitValidator.SERVICE_PROVIDER_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileServiceProvider[].class);
        } else if (MemberProfileTraitValidator.SUBSCRIPTION_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfileSubscription[].class);
        } else if (MemberProfileTraitValidator.PERSONALIZATION_TRAIT_TYPE.equals(traitId)) {
            rs = jp.getCodec().readValue(jp, MemberProfilePersonalization[].class);
        }
        return rs == null ? null : Arrays.asList(rs);
    }
}
