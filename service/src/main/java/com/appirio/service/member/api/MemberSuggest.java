/*
 * Copyright (C) 2016 TopCoder Inc., All Rights Reserved.
 */
package com.appirio.service.member.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The member suggest response data.
 * 
 * @author TCSASSEMBLER
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
public class MemberSuggest {
    
    /**
     * The user id
     */
    @Getter
    @Setter
    private Long userId;
    
    /**
     * The handle
     */
    @Getter
    @Setter
    private String handle;
    
    /**
     * The photo URL
     */
    @Getter
    @Setter
    private String photoURL;
    
    /**
     * The first name
     */
    @Getter
    @Setter
    private String firstName;
    
    /**
     * The last name
     */
    @Getter
    @Setter
    private String lastName;
    
    /**
     * The highest current rating of the user
     */
    @Getter
    @Setter
    private Long maxRating;

}
