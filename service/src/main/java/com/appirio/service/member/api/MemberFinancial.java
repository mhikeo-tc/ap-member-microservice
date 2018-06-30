package com.appirio.service.member.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member financial
 *
 * Created by rakeshrecharla on 7/8/15.
 */
@AllArgsConstructor
@NoArgsConstructor
public class MemberFinancial extends Financial {

    /**
     * Id of the user
     */
    @Getter
    @Setter
    private Long userId;
}