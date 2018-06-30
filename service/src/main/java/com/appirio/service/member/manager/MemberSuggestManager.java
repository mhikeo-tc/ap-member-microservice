/*
 * Copyright (C) 2016 TopCoder Inc., All Rights Reserved.
 */

package com.appirio.service.member.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appirio.service.member.api.MemberSuggest;
import com.appirio.service.member.util.Constants;
import com.appirio.supply.SupplyException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Suggest;
import io.searchbox.core.SuggestResult;

/**
 * The manager to fetch the member suggestions from the elastic search. 
 * 
 * @author TCSASSEMBLER
 * @version 1.0
 */
public class MemberSuggestManager {
    /**
     * The logger
     */
    private Logger logger = LoggerFactory.getLogger(MemberSuggestManager.class);

    /**
     * The default max number of suggestions to fetch
     */
    private static final int DEFAULT_MAX_SUGGESTIONS = 50;
    
    /**
     * The suggestion name
     */
    private static final String HANDLE_SUGGESTION_NAME = "handle-suggestion";
    
    /**
     * The query to fetch the suggestions
     */
    private static final String HANDLE_SUGGESTION_QUERY = "{\n" +
            "\"" + HANDLE_SUGGESTION_NAME + "\": {\n" +
            "    \"text\" : \"%s\",\n" +
            "    \"completion\" : {\n" +
            "      \"size\": " + DEFAULT_MAX_SUGGESTIONS + ",\n" +
            "      \"field\" : \"handleSuggest\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
    
    
    /**
     * The query to search the matched member profiles by ids
     */
    private static final String MEMBER_SEARCH_QUERY = "{\n" +
        "\"fields\": [\"userId\", \"handle\", \"photoURL\", \"lastName\", \"firstName\", \"maxRating.rating\" ],\n" +
        "\"size\": %s,\n" +
        "\"query\": {\n" +
            "\"bool\": {\n" +
                "\"must\": {\n" +
                    "\"ids\": {\n" +
                        "\"type\" : \"profile\",\n" +
                        "\"values\" : [%s]\n" +
                    "}\n" +
                "},\n" +
                "\"must\": {\n" +
                    "\"match\": {\n" +
                        "\"status\": \"ACTIVE\"\n" +
                    "}\n" +
                "}\n" +
            "}\n" +
        "}\n" +
    "}\n";

    /**
     * Logger for the class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberSuggestManager.class);
    
    /**
     * The jest client
     */
    private final JestClient jestClient;
    
    /**
     * The max number of suggestions
     */
    private final int maxSuggestions;
    
    /**
     * Constructor
     * 
     * @param jestClient the jest client
     * @param maxSuggestions the max number of suggestions
     */
    public MemberSuggestManager(JestClient jestClient, Integer maxSuggestions) {
        this.jestClient = jestClient;
        
        this.maxSuggestions = (maxSuggestions != null) ? maxSuggestions : DEFAULT_MAX_SUGGESTIONS;
    }
    
    /**
     * Get member suggests.
     * 
     * @param handlePrefix the prefix of handle
     * @return a list of member suggests
     * @throws IOException if any i/o error occurs
     * @throws SupplyException if fail to get the results from elastic search
     */
    public List<MemberSuggest> getMemberSuggests(String handlePrefix) throws IOException, SupplyException {
        // build suggest query
        String suggestQuery = String.format(HANDLE_SUGGESTION_QUERY, handlePrefix);
        logger.info(suggestQuery);
        Suggest suggest = new Suggest.Builder(suggestQuery)
                .addIndex(Constants.MEMBER_INDEX_NAME).build();
        
        // fetch the suggestions
        SuggestResult suggestResult = jestClient.execute(suggest);
        LOGGER.debug(suggestResult.getJsonString());
        if (!suggestResult.isSucceeded()) {
            throw new SupplyException(suggestResult.getErrorMessage());
        }
        
        // extract userIds from matched suggestions
        List<SuggestResult.Suggestion> suggestions = suggestResult.getSuggestions(HANDLE_SUGGESTION_NAME);
        if (suggestions.isEmpty()) {
            return Collections.emptyList();
        }
        
        SuggestResult.Suggestion suggestion = suggestions.get(0);
//        StringBuilder userIds = new StringBuilder();
        List<MemberSuggest> memberSuggests = new ArrayList<>();

        for (Map<String, Object> opts : suggestion.options) {
            try {
                Map payload = (Map) opts.get("payload");
                MemberSuggest memberSuggest = new MemberSuggest();
                memberSuggest.setUserId(Long.parseLong((String) payload.get("userId")));
                memberSuggest.setHandle((String) payload.get("handle"));
                memberSuggest.setFirstName((String) payload.get("firstName"));
                memberSuggest.setLastName((String) payload.get("lastName"));
                memberSuggest.setPhotoURL((String) payload.getOrDefault("photoURL", ""));
                String maxRating = (String) payload.getOrDefault("maxRating", null);

                if (maxRating != null) {
                    memberSuggest.setMaxRating(Long.parseLong(maxRating));
                }
                memberSuggests.add(memberSuggest);
            } catch(Exception e) {
                // catch the exception, skip the record
                logger.error(e.getMessage());
            }
        }

        return memberSuggests;
    }
    
    /**
     * Get the field value
     * 
     * @param fields the json object
     * @param fieldName the field name
     * @return the field value
     */
    private static String getFieldValue(JsonObject fields, String fieldName) {
        return fields.has(fieldName) ? fields.get(fieldName).getAsString() : null;
    }

}
