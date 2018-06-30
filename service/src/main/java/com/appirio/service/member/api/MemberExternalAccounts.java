package com.appirio.service.member.api;

import com.appirio.supply.dataaccess.api.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member external accounts
 *
 * Created by rakeshrecharla on 9/10/15.
 */
@AllArgsConstructor
@NoArgsConstructor
public class MemberExternalAccounts extends BaseModel {

    /**
     * Id of the user
     */
    @Getter
    @Setter
    private Integer userId;

    /**
     * Handle
     */
    @Getter
    @Setter
    private String handle;

    /**
     * External accounts behance
     */
    @Getter
    @Setter
    private ExternalAccountsBehance behance;

    /**
     * External accounts bit bucket
     */
    @Getter
    @Setter
    private ExternalAccountsBitBucket bitbucket;

    /**
     * External accounts dribbble
     */
    @Getter
    @Setter
    private ExternalAccountsDribbble dribbble;

    /**
     * External accounts github
     */
    @Getter
    @Setter
    private ExternalAccountsGithub github;

    /**
     * External accounts linkedIn
     */
    @Getter
    @Setter
    private ExternalAccountsLinkedIn linkedin;

    /**
     * External accounts stack overflow
     */
    @Getter
    @Setter
    private ExternalAccountsStackOverflow stackoverflow;

    /**
     * External accounts twitter
     */
    @Getter
    @Setter
    private ExternalAccountsTwitter twitter;
}