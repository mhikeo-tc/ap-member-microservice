package com.appirio.service.member.api;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.appirio.service.member.marshaller.MemberProfileAddressMarshaller;
import com.appirio.service.member.marshaller.MemberProfileTraitDataMarshaller;
import com.appirio.service.member.util.RESTResourceDeserializer;
import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

public class MemberProfileTraitData implements RESTResource {
    /**
     * The traitId field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private String traitId;
    /**
     * The data field
     */
    @Getter
    //@Setter
    private List<? extends RESTResource> data;
    
    @JsonDeserialize(using = RESTResourceDeserializer.class)
    public void setData(List<? extends RESTResource> data) {
        this.data = data;
    }
}
