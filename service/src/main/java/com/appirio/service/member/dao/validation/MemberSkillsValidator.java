package com.appirio.service.member.dao.validation;

import com.appirio.service.member.api.MemberEnteredSkills;
import com.appirio.service.member.api.MemberInputSkills;
import com.appirio.service.member.manager.MemberSkillsManager;
import com.appirio.supply.Messages;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rakeshrecharla on 2/29/16.
 */
public class MemberSkillsValidator implements CustomValidator<MemberEnteredSkills> {

    /**
     * Message result
     */
    List<String> result = new ArrayList<String>();

    public List<String> validate(MemberEnteredSkills memberEnteredSkills) {

        Map<Long, MemberInputSkills> memberInputSkillsMap = memberEnteredSkills.getSkills();

        for (Map.Entry<Long, MemberInputSkills> entry : memberInputSkillsMap.entrySet()) {
            Long key = entry.getKey();
            if (!MemberSkillsManager.getTagMap().containsKey(key)) {
                result.add(String.format(Messages.INVALID_TAG_ID, key));
            }
        }
        return result;
    }
}
