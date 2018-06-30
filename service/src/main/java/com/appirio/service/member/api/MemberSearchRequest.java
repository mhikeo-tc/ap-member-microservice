package com.appirio.service.member.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents Member search request
 * @author TCSCODER
 */
public class MemberSearchRequest {

    /**
     * Handle, could be null
     */
    @Getter
    @Setter
    private String handle;

    /**
     * Query default value is MEMBER_SEARCH
     */
    @Getter
    @Setter
    private String query;

    /**
     * Include fields joined with comma
     */
    @Getter
    @Setter
    private String fields;

    /**
     * Status filter
     */
    @Getter
    @Setter
    private String status;

    /**
     * Limit for paging, default value is 11
     */
    @Getter
    @Setter
    private Integer limit;

    /**
     * Offset for paging, default value is 0
     */
    @Getter
    @Setter
    private Integer offset;
}