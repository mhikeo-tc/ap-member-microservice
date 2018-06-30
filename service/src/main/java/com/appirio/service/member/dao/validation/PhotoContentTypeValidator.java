package com.appirio.service.member.dao.validation;

import com.appirio.service.member.api.PhotoContentType;
import com.appirio.supply.Messages;
import com.appirio.supply.SupplyException;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakeshrecharla on 9/3/15.
 */
public class PhotoContentTypeValidator implements CustomValidator<PhotoContentType> {

    /**
     * Message result
     */
    List<String> result = new ArrayList<String>();

    /**
     * Validate and return image type
     */
    private List<String> validateContentType(String contentType) {

        if (contentType == null) {
            return null;
        }

        String[] contentTypeParts = contentType.split("/");
        if (contentTypeParts.length != 2) {
             result.add(String.format(Messages.CONTENT_TYPE_NOT_VALID, contentType));
        }

        if (!contentTypeParts[0].equals("image")) {
            result.add(String.format(Messages.CONTENT_TYPE_NOT_IMAGE, contentType));
        }

        return result;
    }


    public List<String> validate(PhotoContentType photoContentType) throws SupplyException {

        validateContentType(photoContentType.getContentType());
        return result ;
    }
}