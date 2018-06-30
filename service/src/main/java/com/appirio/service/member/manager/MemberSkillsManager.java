package com.appirio.service.member.manager;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.amazonaws.util.json.JSONTokener;
import com.appirio.service.member.api.MemberAggregatedSkills;
import com.appirio.service.member.api.MemberEnteredSkills;
import com.appirio.service.member.api.MemberInputSkills;
import com.appirio.service.member.api.MemberProfile;
import com.appirio.service.member.api.MemberSkills;
import com.appirio.service.member.dao.MemberProfileDAO;
import com.appirio.service.member.dao.MemberSkillsDAO;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.SkillsSource;
import com.appirio.tech.core.auth.AuthUser;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manager for Member skills
 *
 * Created by rakeshrecharla on 7/20/15.
 */
public class MemberSkillsManager {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberStatsManager.class);

    /**
     * DAO for Member skills
     */
    private MemberSkillsDAO memberSkillsDAO;

    /**
     * DAO for Member profile
     */
    private MemberProfileDAO memberProfileDAO;

    /**
     * File service domain
     */
    private String fileServiceDomain;

    /**
     * Api version
     */
    private String apiVersion;

    /**
     * Tag map
     */
    @Getter
    @Setter
    private static Map<Long, String> tagMap = new HashMap<Long, String>();

    /**
     * Constructor to initialize DAOs for member stats and member copilot stats
     * @param memberSkillsDAO       Member skills DAO
     * @param memberProfileDAO      Member profile DAO
     */
    public MemberSkillsManager(MemberSkillsDAO memberSkillsDAO, MemberProfileDAO memberProfileDAO,
                               String fileServiceDomain, String apiVersion) throws SupplyException {
        this.memberSkillsDAO = memberSkillsDAO;
        this.memberProfileDAO = memberProfileDAO;
        this.fileServiceDomain = fileServiceDomain;
        this.apiVersion = apiVersion;

        // Get all the tags for the domain SKILL
        getAllTags();
    }

    /**
     * Get all tags from tagging microservice
     * @throws SupplyException      Supply exception
     */
    private void getAllTags() throws SupplyException {
        try {
            JSONObject result = makeRequest();
            if (result != null) {
                JSONArray content = result.getJSONArray("content");
                for (int i = 0; i < content.length(); i++) {
                    JSONObject element = (JSONObject) content.get(i);
                    Long tagId = element.getLong("id");
                    String tagName = element.getString("name");
                    tagMap.put(tagId, tagName);
                }
            }
        } catch (JSONException e) {
            throw new SupplyException("JSON exception " + e.getMessage());
        }
    }

    /**
     * Make request to tagging microservice
     * @return JSONObject       JSON object
     * @throws JSONException    JSON Exception
     */
    private JSONObject makeRequest() throws JSONException {
        Client client =  ClientBuilder.newClient();
        WebTarget res = null;
        JSONObject result = null;

        try {
            String taggingServiceURL = "http://" + fileServiceDomain + "/" +
                    apiVersion + "/tags/?filter=domain%3DSKILLS";

            res = client.target(taggingServiceURL);
            String resp = res.request().get(String.class);
            JSONObject response = new JSONObject(new JSONTokener(resp));
            result = response.getJSONObject("result");

        }catch(Exception e) {
            logger.error("Exception in contacting tagging service "+e.getMessage());
        }

        return result;
    }

    /**
     * Get member Aggregated skills
     * @param handle                    Handle of the user
     * @return MemberAggregatedSkills   Member aggregated skills
     */
    public MemberAggregatedSkills getMemberAggregatedSkills(String handle) throws SupplyException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, null, true);

        MemberEnteredSkills memberEnteredSkills = memberSkillsDAO.getMemberEnteredSkills(memberProfile.getUserId());
        MemberAggregatedSkills memberAggregatedSkills = memberSkillsDAO.getMemberAggregatedSkills(memberProfile.getUserId());

        if (memberAggregatedSkills == null) {
            memberAggregatedSkills = new MemberAggregatedSkills();
        }

        Map<Long, MemberSkills> memberAggregatedSkillsMap = memberAggregatedSkills.getSkills();
        if (memberAggregatedSkillsMap == null) {
            memberAggregatedSkillsMap = new HashMap<Long, MemberSkills>();
        }

        if (memberEnteredSkills != null) {
            Map<Long, MemberInputSkills> memberInputSkillsMap = memberEnteredSkills.getSkills();
            for (Map.Entry<Long, MemberInputSkills> entry : memberInputSkillsMap.entrySet()) {
                MemberSkills memberSkills = null;
                Long key = entry.getKey();
                MemberInputSkills value = entry.getValue();

                logger.debug("memberInputSkills tag Id : " + key);
                if (!memberAggregatedSkillsMap.containsKey(key)) {
                    memberSkills = new MemberSkills();
                    memberSkills.setScore(1D);
                    memberSkills.setSources(new HashSet<String>(Arrays.asList(
                            SkillsSource.USER_ENTERED.toString())));
                } else {
                    memberSkills = memberAggregatedSkillsMap.get(key);
                    Set<String> sources = memberSkills.getSources();
                    sources.add(SkillsSource.USER_ENTERED.toString());
                    memberSkills.setSources(sources);
                }
                memberSkills.setHidden(value.getHidden());
                memberAggregatedSkillsMap.put(key, memberSkills);
            }
            memberAggregatedSkills.setUpdatedAt(memberEnteredSkills.getUpdatedAt());
            memberAggregatedSkills.setUpdatedBy(memberEnteredSkills.getUpdatedBy());
        }

        // Remove hidden skills
        Map<Long, MemberSkills> resultMap = new HashMap<Long, MemberSkills>();
        for (Map.Entry<Long, MemberSkills> entry : memberAggregatedSkillsMap.entrySet()) {
            Long key = entry.getKey();
            MemberSkills value = entry.getValue();
            if (tagMap.containsKey(key)) {
                value.setTagName(tagMap.get(key));
                if (value.getHidden() != null && value.getHidden() == false) {
                    logger.debug("MemberSkills tagId : " + key + " tagName : " + value.getTagName());
                    resultMap.put(key, value);
                }
            }
        }

        memberAggregatedSkills.setUserId(memberProfile.getUserId());
        memberAggregatedSkills.setHandleLower(memberProfile.getHandleLower());
        memberAggregatedSkills.setUserHandle(memberProfile.getHandle());
        memberAggregatedSkills.setSkills(resultMap);

        return memberAggregatedSkills;
    }

    /**
     * Update member skills
     * @param handle                Handle of the user
     * @param authUser              Authentication user
     * @param memberEnteredSkills   Member entered skills
     * @return                      Member aggregated skills
     * @throws SupplyException      Exception for the supply
     */
    public MemberAggregatedSkills updateMemberSkills(String handle, AuthUser authUser, MemberEnteredSkills memberEnteredSkills)
            throws SupplyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        MemberProfile memberProfile = memberProfileDAO.validateHandle(handle, authUser, false);
        MemberEnteredSkills existingMemberEnteredSkills = memberSkillsDAO.getMemberEnteredSkills(memberProfile.getUserId());

        if (existingMemberEnteredSkills != null && existingMemberEnteredSkills.getSkills() != null) {
            Map<Long, MemberInputSkills> existingMemberInputSkillsMap = existingMemberEnteredSkills.getSkills();
            Map<Long, MemberInputSkills> memberInputSkillsMap = memberEnteredSkills.getSkills();

            for (Map.Entry<Long, MemberInputSkills> entry : memberInputSkillsMap.entrySet()) {
                MemberInputSkills memberInputSkills = null;
                Long key = entry.getKey();
                MemberInputSkills value = entry.getValue();

                if (!existingMemberInputSkillsMap.containsKey(key)) {
                    memberInputSkills = new MemberInputSkills();
                }
                else {
                    memberInputSkills = existingMemberInputSkillsMap.get(key);
                }
                memberInputSkills.setHidden(value.getHidden());
                existingMemberInputSkillsMap.put(key, memberInputSkills);
            }
            existingMemberEnteredSkills.setSkills(existingMemberInputSkillsMap);
            memberEnteredSkills = existingMemberEnteredSkills;
        }

        memberEnteredSkills.setUserId(memberProfile.getUserId());
        memberEnteredSkills.setUserHandle(memberProfile.getHandle());
        memberEnteredSkills.setHandleLower(memberProfile.getHandleLower());
        memberEnteredSkills.setUpdatedBy(authUser.getUserId().toString());
        memberEnteredSkills.setUpdatedAt(new Date());
        memberSkillsDAO.updateMemberSkills(memberEnteredSkills);

        return getMemberAggregatedSkills(memberEnteredSkills.getUserHandle());
    }
}
