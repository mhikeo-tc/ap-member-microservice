package com.appirio.service.member.api;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.appirio.service.member.dao.validation.MemberProfileValidator;
import com.appirio.service.member.marshaller.MaxRatingMarshaller;
import com.appirio.service.member.marshaller.MemberProfileAddressMarshaller;
import com.appirio.supply.SupplyException;
import com.appirio.supply.ValidationException;
import com.appirio.supply.dataaccess.api.validation.ValidateEmail;
import com.appirio.supply.dataaccess.api.validation.ValidateNotNull;
import com.appirio.supply.dataaccess.api.validation.ValidateNotZero;
import com.appirio.supply.dataaccess.api.validation.Validator;
import com.appirio.supply.dataaccess.api.validation.customvalidator.CustomValidator;
import com.appirio.tech.core.api.v3.resource.old.RESTResource;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents the MemberProfile model 
 * 
 * <p>
 * Changes in the version 1.1 (Topcoder Member Service - New Endpoint to Update Email Address Code Challenge v1.0)
 * - add the fields to support the email verificaition
 * </p>
 * 
 * @author TCCoder
 * @version 1.1
 *
 */
@DynamoDBTable(tableName="MemberProfile")
@AllArgsConstructor
@NoArgsConstructor
@Validator(MemberProfileValidator.class)
public class MemberProfile implements RESTResource {
    /**
     * Set createdAt epoch time
     * @param value
     */
    @JsonIgnore
    public void setCreatedAt(Date value) {
        createdAt = value;
    }
   /**
     * Set updatedAt epoch time
     * @param value
     */
    @JsonIgnore
    public void setUpdatedAt(Date value) {
        updatedAt = value;
    }

    /**
     * Get createdAt epoch time
     * @return Long
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="createdAt")
    public Long getCreatedAt() {
        return createdAt == null ? null : createdAt.getTime();
    }

    /**
     * Set createdAt epoch time
     * @param value
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="createdAt")
    public void setCreatedAt(Long value) {
        createdAt = new Date(value);
	}

    /**
     * Get updatedAt epoch time
     * @return Long
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="updatedAt")
    public Long getUpdatedAt() {
        return updatedAt == null ? null : updatedAt.getTime();
	}

    /**
     * Set updatedAt epoch time
     * @param value
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName="updatedAt")
    public void setUpdatedAt(Long value) {
        updatedAt = new Date(value);
    }

    /**
     * Max rating of the member
     */
    @DynamoDBIgnore
    @Getter
    @Setter
    @DynamoDBMarshalling(marshallerClass=MaxRatingMarshaller.class)
    public MaxRating maxRating;

    /**
     * Id of the user
     */
    @DynamoDBHashKey
    @Getter
    @Setter
    private Integer userId;

    /**
     * First name
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    @ValidateNotNull
    private String firstName;

    /**
     * Last name
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    @ValidateNotNull
    private String lastName;

    /**
     * Description
     */
    @Getter
    @Setter
    private String description;

    /**
     * Other language name
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private String otherLangName;

    /**
     * Handle
     */
    @Getter
    @Setter
    @ValidateNotNull
    private String handle;

    /**
     * Handle Lower
     */
    @DynamoDBIndexHashKey(globalSecondaryIndexName="handleLower-index", attributeName="handleLower")
    @JsonIgnore
    @Getter
    @Setter
    private String handleLower;

    /**
     * Status
     */
    @JsonIgnore
    @Getter
    @Setter
    private String status;

    /**
     * Email
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    @ValidateNotNull
    private String email;
    
    /**
     * The new email field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private String newEmail;
    
    /**
     * The email verify token field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private String emailVerifyToken;
    
    /**
     * The email verify token date field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm'Z'", timezone="UTC")
    @Getter
    @Setter
    private Date emailVerifyTokenDate;

    /**
     * Address
     */
    @DynamoDBMarshalling(marshallerClass = MemberProfileAddressMarshaller.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private List<MemberProfileAddress> addresses;

    /**
     * Home country code
     */
    @Getter
    @Setter
    private String homeCountryCode;

    /**
     * Competition country code
     */
    @Getter
    @Setter
    private String competitionCountryCode;

    /**
     * Member photo
     */
    @Getter
    @Setter
    private String photoURL;

    /**
     * Track list
     */
    @Getter
    @Setter
    private List<String> tracks;


    //  Adapting properties from BaseModel

    /**
     * Date in which this record was last updated
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm'Z'", timezone="UTC")
    private Date updatedAt;

    /**
     * Date in which this record was created
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm'Z'", timezone="UTC")
    private Date createdAt;

    /**
     * Identifier of user whom created the record
     */
    @Getter
    @Setter
    private String createdBy;

    /**
     * Identifier of user whom performed the last update in this record
     */
    @Getter
    @Setter
    private String updatedBy;

    /**
     * Performs validation of this object based on annotations such as
     * ValidateNotNull, ValidateNotZero, ValidateEmail etc.
     *
     * This is automatically invoked by DAOs if the method is annotated with the
     * Validate annotation. See Validate for more information
     *
     * @return List of validation error messages, if the result is a empty list no errors
     *          were found
     */
    public List<String> validate() throws IllegalArgumentException,
            IllegalAccessException, NoSuchMethodException, SecurityException,
            ValidationException, InvocationTargetException, InstantiationException, SupplyException {
        // List of error messages
        List<String> result = new ArrayList<String>();

        // Loops through all fields looking for validation annotations
        for (Field field : this.getClass().getDeclaredFields()) {
            ValidateNotNull validateNotNull = field
                    .getAnnotation(ValidateNotNull.class);
            ValidateNotZero validateNotZero = field
                    .getAnnotation(ValidateNotZero.class);
            ValidateEmail validateEmail = field
                    .getAnnotation(ValidateEmail.class);

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
                } else if(value instanceof Integer) {
                    if(((Integer) value).intValue() == 0) {
                        result.add("Field " + field.getName() + " must not be zero");
                    }
                } else if(value instanceof Long) {
                    if(((Long) value).longValue() == 0) {
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

        for(Annotation annotation : this.getClass().getAnnotations()){
            if(annotation instanceof Validator){
                Validator validator = (Validator) annotation;
                for(Class customValidatorClass : validator.value()) {
                    if(CustomValidator.class.isAssignableFrom(customValidatorClass)) {
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
     * @param fieldName            field name
     * @return                     Method instance
     * @throws ValidationException In case getter is not found
     */
    private Method findGetter(String fieldName) throws ValidationException {
        char[] stringArray = fieldName.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        String capitalizedFieldName = new String(stringArray);

        for (Method method : this.getClass().getMethods()) {
            if ((method.getName().equals("get" + capitalizedFieldName) || method
                    .getName().equals("is" + capitalizedFieldName))
                    && method.getParameterCount() == 0) {
                return method;
            }
        }

        // TODO: Externalize this string into a properties file
        throw new ValidationException("Getter not found for field " + fieldName
                + " which has a validation annotation");

    }
}