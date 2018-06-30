package com.appirio.service.member.api;

import com.appirio.service.member.dao.validation.PhotoContentTypeValidator;
import com.appirio.supply.dataaccess.api.BaseModel;
import com.appirio.supply.dataaccess.api.validation.ValidateNotNull;
import com.appirio.supply.dataaccess.api.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Photo content type
 *
 * Created by rakeshrecharla on 9/3/15.
 */
@AllArgsConstructor
@NoArgsConstructor
@Validator(PhotoContentTypeValidator.class)
public class PhotoContentType extends BaseModel {

    /**
     * Content type
     */
    @Getter
    @Setter
    @ValidateNotNull
    private String contentType;
}