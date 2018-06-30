package com.appirio.service.member.api;

import com.appirio.service.member.dao.validation.PhotoContentTypeValidator;
import com.appirio.supply.dataaccess.api.validation.ValidateNotNull;
import com.appirio.supply.dataaccess.api.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Photo token and content type
 *
 * Created by rakeshrecharla on 8/3/15.
 */
@AllArgsConstructor
@NoArgsConstructor
@Validator(PhotoContentTypeValidator.class)
public class PhotoTokenContentType extends PhotoContentType {

    /**
     * Epoch token
     */
    @Getter
    @Setter
    @ValidateNotNull
    private Long token;
}