package com.appirio.service.member.api;

import com.appirio.supply.dataaccess.api.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by rakeshrecharla on 1/20/16.
 */
public class Financial extends BaseModel {

    /**
     * Gross amount
     */
    @Getter
    @Setter
    private Double amount;

    /**
     * Payment status
     */
    @Getter
    @Setter
    private String status;
}
