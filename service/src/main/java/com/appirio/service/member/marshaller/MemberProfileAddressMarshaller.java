package com.appirio.service.member.marshaller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.appirio.service.member.api.MemberProfileAddress;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

import static com.amazonaws.util.Throwables.failure;


/**
 * Marshaller for member profile address
 *
 * Created by rakeshrecharla on 8/7/15.
 */
public class MemberProfileAddressMarshaller implements DynamoDBMarshaller<List<MemberProfileAddress>> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writer();

    @Override
    public String marshall(List<MemberProfileAddress> obj) {

        try {
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw failure(e,
                    "Unable to marshall the instance of " + obj.getClass()
                            + "into a string");
        }
    }

    @Override
    public List<MemberProfileAddress> unmarshall(Class<List<MemberProfileAddress>> clazz, String json) {
        final CollectionType
                type =
                mapper.getTypeFactory().constructCollectionType(List.class, MemberProfileAddress.class);
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw failure(e, "Unable to unmarshall the string " + json
                    + "into " + clazz);
        }
    }
}