package com.appirio.service.member.api;

import com.appirio.supply.dataaccess.api.BaseModel;
import com.appirio.supply.dataaccess.api.validation.ValidateNotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Photo token and pre signed URL
 *
 * Created by rakeshrecharla on 8/26/15.
 */
@AllArgsConstructor
@NoArgsConstructor
public class PhotoTokenURL extends BaseModel {

    /**
     * Pre signed Url
     */
    @Getter
    @Setter
    @ValidateNotNull
    private String preSignedURL;

    /**
     * Epoch token
     */
    @Getter
    @Setter
    @ValidateNotNull
    private Long token;
}