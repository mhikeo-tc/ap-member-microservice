package com.appirio.service.member.api;

import com.appirio.supply.dataaccess.api.BaseModel;
import com.appirio.supply.dataaccess.api.validation.ValidateNotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents External link URL
 *
 * Created by rakeshrecharla on 10/19/15.
 */
@AllArgsConstructor
@NoArgsConstructor
public class ExternalLinkURL extends BaseModel {

    /**
     * External link url
     */
    @Getter
    @Setter
    @ValidateNotNull
    private String url;
}