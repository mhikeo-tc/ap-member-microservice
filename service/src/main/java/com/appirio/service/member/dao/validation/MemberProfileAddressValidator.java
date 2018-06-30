package com.appirio.service.member.dao.validation;

import com.appirio.service.member.api.MemberProfileAddress;
import com.appirio.supply.Messages;
import com.appirio.supply.SupplyException;
import com.appirio.supply.constants.Address;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakeshrecharla on 7/16/15.
 */
public class MemberProfileAddressValidator implements CustomValidator<MemberProfileAddress> {

    /**
     * Message result
     */
    List<String> result = new ArrayList<String>();

    /**
     * Validate address type
     * @param addressType address type
     */
    private String validateAddressType(String addressType) {

        if (addressType == null) {
            return Address.HOME.toString();
        }

        if (!Address.isValid(addressType.toUpperCase())) {
            result.add(String.format(Messages.ADDRESS_TYPE_NOT_VALID, addressType));
        }

        return addressType.toUpperCase();
    }

    public List<String> validate(MemberProfileAddress memberProfileAddress) throws SupplyException {

        memberProfileAddress.setType(validateAddressType(memberProfileAddress.getType()));

        return result;
    }
}