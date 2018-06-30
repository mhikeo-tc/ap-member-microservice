package com.appirio.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * The EmailVerificationConfiguration is for configuration.
 *
 * It's added in Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0
 *
 * @author TCCoder
 * @version 1.0
 */
public class EmailVerificationConfiguration {
    /**
     * Represents the subject attribute.
     */
    @JsonProperty
    @NotEmpty
    @Getter
    @Setter
    private String subject;

    /**
     * Represents the verification agree url attribute.
     */
    @JsonProperty
    @NotEmpty
    @Getter
    @Setter
    private String verificationAgreeUrl;

    /**
     * Represents the token expire time in minutes attribute.
     */
    @JsonProperty
    @NotNull
    @Getter
    @Setter
    private Integer tokenExpireTimeInMinutes;

}
