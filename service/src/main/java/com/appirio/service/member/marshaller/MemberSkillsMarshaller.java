package com.appirio.service.member.marshaller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.appirio.service.member.api.MemberSkills;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.Map;

import static com.amazonaws.util.Throwables.failure;

/**
 * Marshaller for member skills
 *
 * Created by rakeshrecharla on 8/31/15.
 */
public class MemberSkillsMarshaller implements DynamoDBMarshaller<Map<Long, MemberSkills>> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer = mapper.writer();

    @Override
    public String marshall(Map<Long, MemberSkills> obj) {

        try {
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw failure(e,
                    "Unable to marshall the instance of " + obj.getClass()
                            + "into a string");
        }
    }

    @Override
    public Map<Long, MemberSkills> unmarshall(Class<Map<Long, MemberSkills>> clazz, String json) {
        final MapType type =
                mapper.getTypeFactory().constructMapType(Map.class, Long.class, MemberSkills.class);
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw failure(e, "Unable to unmarshall the string " + json
                    + "into " + clazz);
        }
    }
}