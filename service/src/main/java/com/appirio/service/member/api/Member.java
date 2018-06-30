package com.appirio.service.member.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neovisionaries.i18n.CountryCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents Member for search
 *
 * Created by rakeshrecharla on 1/19/16.
 */
@AllArgsConstructor
@NoArgsConstructor
public class Member extends MemberProfile {

    /**
     * Number of challenges
     */
    @Getter
    @Setter
    private Long challenges;

    /**
     * Number of wins
     */
    @Getter
    @Setter
    private Long wins;

    /**
     * Financial information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private Financial financial;

    /**
     * Skills
     */
    @Getter
    @Setter
    private List<MemberSkillsTags> skills;

    /**
     * Returns the name of the competition country, as a convenience for the frontend
     * @return
     */
    public String getCompetitionCountryName() {
        if(getCompetitionCountryCode() != null) {
            CountryCode code = CountryCode.getByCode(getCompetitionCountryCode());

            if(code != null) {
                return code.getName();
            }
        }

        return null;
    }

    /**
     * Returns the name of the competition country, as a convenience for the frontend
     * @return
     */
    public String getHomeCountryName() {
        if(getHomeCountryCode() != null) {
            CountryCode code = CountryCode.getByCode(getHomeCountryCode());

            if(code != null) {
                return code.getName();
            }
        }

        return null;
    }

}