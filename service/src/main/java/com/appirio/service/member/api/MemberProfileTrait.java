package com.appirio.service.member.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.appirio.service.member.dao.validation.MemberProfileTraitValidator;
import com.appirio.service.member.marshaller.MemberProfileTraitDataMarshaller;
import com.appirio.supply.SupplyException;
import com.appirio.supply.ValidationException;
import com.appirio.supply.dataaccess.api.validation.ValidateEmail;
import com.appirio.supply.dataaccess.api.validation.ValidateNotNull;
import com.appirio.supply.dataaccess.api.validation.ValidateNotZero;
import com.appirio.supply.dataaccess.api.validation.Validator;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;
import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the MemberProfileTrait model
 * 
 * It's added in Topcoder Member Service - Add Additional Traits v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
@DynamoDBTable(tableName = "MemberProfileTrait")
@NoArgsConstructor
@Validator(MemberProfileTraitValidator.class)
public class MemberProfileTrait implements RESTResource {
    /**
     * The userId field
     */
    @Getter
    @Setter
    @DynamoDBHashKey
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;


    /**
     * The traitId field
     */
    @Getter
    @Setter
    @DynamoDBRangeKey
    @ValidateNotNull
    private String traitId;


    /**
     * The categoryName field
     */
    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "categoryName")
    @ValidateNotNull
    private String categoryName;


    /**
     * The createdAt field
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'", timezone = "UTC")
    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "createdAt")
    private Date createdAt;


    /**
     * The createdBy field
     */
    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "createdBy")
    private Long createdBy;


    /**
     * The updatedAt field
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'", timezone = "UTC")
    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "updatedAt")
    private Date updatedAt;


    /**
     * The updatedBy field
     */
    @Getter
    @Setter
    @DynamoDBAttribute(attributeName = "updatedBy")
    private Long updatedBy;


    /**
     * The traits field
     */
    @Getter
    @Setter
    @DynamoDBMarshalling(marshallerClass = MemberProfileTraitDataMarshaller.class)
    private MemberProfileTraitData traits;

    /**
     * Performs validation of this object based on annotations such as
     * ValidateNotNull, ValidateNotZero, ValidateEmail etc.
     *
     * This is automatically invoked by DAOs if the method is annotated with the
     * Validate annotation. See Validate for more information
     *
     * @return List of validation error messages, if the result is a empty list
     *         no errors were found
     */
    public List<String> validate() throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ValidationException,
            InvocationTargetException, InstantiationException, SupplyException {
        // List of error messages
        List<String> result = new ArrayList<String>();

        // Loops through all fields looking for validation annotations
        for (Field field : this.getClass().getDeclaredFields()) {
            ValidateNotNull validateNotNull = field.getAnnotation(ValidateNotNull.class);
            ValidateNotZero validateNotZero = field.getAnnotation(ValidateNotZero.class);
            ValidateEmail validateEmail = field.getAnnotation(ValidateEmail.class);

            // Process each validation annotation
            if (validateNotNull != null) {
                Method getter = findGetter(field.getName());

                if (getter.invoke(this) == null) {
                    // TODO: Externalize this string into a properties file
                    result.add("Field " + field.getName() + " must not be null");
                }
            }

            if (validateNotZero != null) {
                Method getter = findGetter(field.getName());

                Object value = getter.invoke(this);

                if (value == null) {
                    // TODO: Externalize this string into a properties file
                    result.add("Field " + field.getName() + " must not be null");
                } else if (value instanceof Integer) {
                    if (((Integer) value).intValue() == 0) {
                        result.add("Field " + field.getName() + " must not be zero");
                    }
                } else if (value instanceof Long) {
                    if (((Long) value).longValue() == 0) {
                        result.add("Field " + field.getName() + " must not be zero");
                    }
                } else {
                    result.add("Type of " + field.getName() + " doesn't support not zero validation");
                }
            }

            if (validateEmail != null) {
                Method getter = findGetter(field.getName());

                Object value = getter.invoke(this);

                if (!EmailValidator.getInstance().isValid((String) value)) {
                    result.add("Field " + field.getName() + " is not valid");
                }
            }
        }

        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof Validator) {
                Validator validator = (Validator) annotation;
                for (Class customValidatorClass : validator.value()) {
                    if (CustomValidator.class.isAssignableFrom(customValidatorClass)) {
                        CustomValidator customValidator = (CustomValidator) customValidatorClass.newInstance();
                        result.addAll(customValidator.validate(this));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Given a field name, finds its getter method
     * 
     * @param fieldName field name
     * @return Method instance
     * @throws ValidationException In case getter is not found
     */
    private Method findGetter(String fieldName) throws ValidationException {
        char[] stringArray = fieldName.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        String capitalizedFieldName = new String(stringArray);

        for (Method method : this.getClass().getMethods()) {
            if ((method.getName().equals("get" + capitalizedFieldName) || method.getName().equals("is" + capitalizedFieldName))
                    && method.getParameterCount() == 0) {
                return method;
            }
        }

        // TODO: Externalize this string into a properties file
        throw new ValidationException("Getter not found for field " + fieldName + " which has a validation annotation");

    }
}
