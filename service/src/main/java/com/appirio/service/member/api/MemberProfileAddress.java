package com.appirio.service.member.api;

import com.appirio.service.member.dao.validation.MemberProfileAddressValidator;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.appirio.supply.dataaccess.api.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member profile address
 *
 * Created by rakeshrecharla on 7/16/15.
 */
@AllArgsConstructor
@NoArgsConstructor
@Validator(MemberProfileAddressValidator.class)
public class MemberProfileAddress extends BaseModel {

    /**
     * Street Address
     */
    @Getter
    @Setter
    private String streetAddr1;

    /**
     * Street Address
     */
    @Getter
    @Setter
    private String streetAddr2;

    /**
     * City
     */
    @Getter
    @Setter
    private String city;

    /**
     * Zip
     */
    @Getter
    @Setter
    private String zip;

    /**
     * State code
     */
    @Getter
    @Setter
    private String stateCode;

    /**
     * Address type
     */
    @Getter
    @Setter
    private String type;
}