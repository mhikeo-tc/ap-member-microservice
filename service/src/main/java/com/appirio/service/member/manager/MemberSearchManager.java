
package com.appirio.service.member.manager;

import com.appirio.service.member.api.MemberSearchRequest;
import com.appirio.service.member.util.Constants;
import com.appirio.supply.SupplyException;
import com.appirio.supply.dataaccess.QueryResult;
import com.appirio.tech.core.api.v3.request.FilterParameter;
import com.appirio.tech.core.api.v3.request.QueryParameter;
import com.appirio.tech.core.auth.AuthUser;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import org.eclipse.jetty.util.URIUtil;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.NestedFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;


/**
 * Manager for Member search
 *
 * @author TCSCODER
 */
public class MemberSearchManager {
    /**
     * The default max number of documents to fetch
     */
    private static final int DEFAULT_LIMIT = 11;

    /**
     * The default max number of documents for admin users to fetch
     */
    private static final int DEFAULT_ADMIN_LIMIT = 50;

    /**
     * The admin search query type
     */
    private static final String ADMIN_SEARCH = "SEARCH";

    /**
     * The member search query type
     */
    private static final String MEMBER_SEARCH = "MEMBER_SEARCH";

    /**
     * The member skills search query type
     */
    private static final String MEMBER_SKILL = "MEMBER_SKILL";

    /**
     * The error message if query or handle is missing
     */
    private static final String MISSING_ERROR = "400_BAD_REQUEST: 'query' & 'handle' are required";

    /**
     * The error message if error to cast json
     */
    private static final String ERROR_CAST = "Error to cast json";

    /**
     * The error message if query is invalid
     */
    private static final String ERROR_QUERY = "The query is invalid";

    /**
     * The error message if error to search documents in elasticsearch
     */
    private static final String ERROR_SEARCH = "Error to search documents in elasticsearch";
    /**
     * The key name for tracks
     */
    public static final String TRACKS = "tracks";

    /**
     * The key name for skills
     */
    public static final String SKILLS = "skills";

    /**
     * The key name for wins
     */
    public static final String WINS = "wins";

    /**
     * The key name for max rating
     */
    public static final String MAX_RATING = "maxRating";

    /**
     * The default max rating when it is missing
     */
    public static final HashMap<String, Long> DEFAULT_MAX_RATING = new HashMap<String, Long>() {
        {
            put("rating", 0L);
        }
    };

    /**
     * The key name for stats
     */
    public static final String STATS = "stats";

    /**
     * The status key name for copilot
     */
    public static final String COPILOT = "COPILOT";

    /**
     * The status key name for design
     */
    public static final String DESIGN = "DESIGN";

    /**
     * The default design status
     */
    public static final HashMap<String, Object> DEFAULT_DESIGN_STATS = new HashMap<String, Object>() {
        {
            put("wins", 0);
            put("mostRecentSubmission", 0);
            put("challenges", 0);
            put("subTracks", Collections.emptyList());
            put("mostRecentEventDate", 0);
        }
    };

    /**
     * The default copilot status
     */
    public static final HashMap<Object, Object> DEFAULT_COPILOT_STATS = new HashMap<>();

    /**
     * The status key for develop
     */
    public static final String DEVELOP = "DEVELOP";

    /**
     * The default develop status
     */
    public static final HashMap<String, Object> DEFAULT_DEVELOP_STATS = new HashMap<String, Object>() {
        {
            put("wins", 0);
            put("mostRecentSubmission", 0);
            put("challenges", 0);
            put("subTracks", Collections.emptyList());
            put("mostRecentEventDate", 0);
        }
    };

    /**
     * The status key name for data science
     */
    public static final String DATA_SCIENCE = "DATA_SCIENCE";

    /**
     * The default data science status
     */
    public static final HashMap<String, Object> DEFAULT_DATA_SCIENCE_STATS = new HashMap<String, Object>() {
        {
            put("wins", 0);
            put("challenges", 0);
            put("MARATHON_MATCH", new HashMap<String, Object>() {
                {
                    put("wins", 0);
                    put("challenges", 0);
                    put("rank", new HashMap<String, Object>() {
                        {
                            put("maximumRating", 0);
                            put("rating", 0);
                            put("avgRank", 0);
                            put("rank", 0);
                            put("countryRank", 0);
                            put("bestRank", 0);
                        }
                    });
                    put("mostRecentEventName", null);
                }
            });
            put("SRM", new HashMap<String, Object>() {
                {
                    put("wins", 0);
                    put("challenges", 0);
                    put("rank", new HashMap<String, Object>() {
                        {
                            put("minimumRating", 0);
                            put("maximumRating", 0);
                            put("rating", 0);
                            put("rank", 0);
                            put("countryRank", 0);
                        }
                    });
                    put("mostRecentEventName", null);
                }
            });
        }
    };

    /**
     * The default empty list
     */
    public static final List<Object> EMPTY_LIST = Collections.emptyList();

    /**
     * The role name for admin
     */
    public static final String ADMINISTRATOR = "administrator";

    /**
     * The query to search all documents
     */
    public static final String ALL_QUERY = "*";

    /**
     * The plus to connect different queries
     */
    public static final char PLUS = '+';

    /**
     * The split for query string
     */
    public static final char WHITESPACE = ' ';

    /**
     * The split for fields
     */
    public static final String COMMA = ",";

    /**
     * The active status name
     */
    private static String ACTIVE_STATUS = "active";

    /**
     * The key name for photo url
     */
    private static final String PHOTO_URL = "photoURL";

    /**
     * The key name for description
     */
    private static final String DESCRIPTION = "description";

    /**
     * The key name for status
     */
    private static final String STATUS = "status";

    /**
     * The key name for handle
     */
    private static final String HANDLE = "handle";

    /**
     * The term name for handle phase
     */
    private static final String HANDLE_PHRASE = "handle.phrase";

    /**
     * The key name for skill name
     */
    private static final String SKILLS_NAME = "skills.name";

    /**
     * The key name for skill id
     */
    private static final String SKILLS_ID = "skills.id";

    /**
     * The key name skill score for sorting
     */
    private static final String SKILLS_SCORE = "skills.score";

    /**
     * The default includes(must exists in response and will set default value if not present)
     */
    private static final String[] DEFAULT_INCLUDES = {SKILLS, STATS, TRACKS, WINS, MAX_RATING};

    /**
     * The include fields for member search
     */
    private static final String[] MEMBER_SEARCH_INCLUDES = {"createdAt", "tracks", "competitionCountryCode", "wins",
            "userId", "handle", "maxRating", "skills.name", "skills.score", "stats", "photoURL", "description"};

    /**
     * The excludes for member search
     */
    private static final String[] MEMBER_SEARCH_EXCLUDES = {"addresses", "financial", "lastName", "firstName", "email",
            "otherLangName"};

    /**
     * Default excludes for admin search(last words must be email)
     */
    private static final String[] SEARCH_EXCLUDES = {"addresses", "stats", "financial", "otherLangName", "email"};

    /**
     * If admin role should exclude email(must be last word in SEARCH_EXCLUDES
     */
    private static final String[] SEARCH_EXCLUDES_WITHOUT_EMAIL = Arrays.copyOfRange(SEARCH_EXCLUDES, 0,
            SEARCH_EXCLUDES.length - 1);

    /**
     * Logger for the class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberSearchManager.class);

    /**
     * Jest client to search documents.
     */
    private JestClient client;

    /**
     * Default limit for Elasticsearch REST service from configuration
     */
    private Integer elasticsearchDefaultLimit;

    /**
     * Default admin limit for Elasticsearch REST service from configuration
     */
    private Integer elasticsearchDefaultAdminLimit;

    /**
     * Flag to enable cache for nested filter of elasticsearch
     */
    @Getter
    @Setter
    private Boolean elasticsearchCacheNestedFilter;

    /**
     * Constructor to initialize member search manager
     *
     * @param client
     *            The jest client
     * @param elasticsearchDefaultLimit
     *            The default limit for elasticsearch
     * @param elasticsearchDefaultAdminLimit
     *            The default admin limit for elasticsearch
     */
    public MemberSearchManager(JestClient client, Integer elasticsearchDefaultLimit,
                               Integer elasticsearchDefaultAdminLimit, Boolean elasticsearchCacheNestedFilter) {
        this.client = client;
        this.elasticsearchDefaultLimit = elasticsearchDefaultLimit;
        this.elasticsearchDefaultAdminLimit = elasticsearchDefaultAdminLimit;
        this.elasticsearchCacheNestedFilter = elasticsearchCacheNestedFilter;
    }

    /**
     * Check whether given role has administrator role.
     *
     * @param user
     *            the auth user
     * @return true if user is not null and has administrator role
     */
    private static boolean isAdmin(AuthUser user) {
        return user != null && user.hasRole(ADMINISTRATOR);
    }

    /**
     * Decode uri component
     *
     * @param value
     *            the string value
     * @return the decoded string value
     */
    private static String decodeURIComponent(String value) {
        return value == null ? null : URIUtil.decodePath(value);
    }

    /**
     * Fix default value for source map
     *
     * @param sourceMap
     *            the source map
     * @param fields
     *            the include fields
     */
    private static void fixObject(Map<String, Object> sourceMap, List<String> fields) {
        // remove unused id or you can add as id of document
        sourceMap.remove(JestResult.ES_METADATA_ID);
        Predicate<String> checkMissng = (s) -> fields.contains(s) && !sourceMap.containsKey(s);

        // Temporary default values until default values can be set with logstash
        if (checkMissng.test(TRACKS)) {
            sourceMap.put(TRACKS, EMPTY_LIST);
        }
        if (checkMissng.test(SKILLS)) {
            sourceMap.put(SKILLS, EMPTY_LIST);
        }
        if (checkMissng.test(WINS)) {
            sourceMap.put(WINS, 0);
        }
        if (checkMissng.test(MAX_RATING)) {
            sourceMap.put(MAX_RATING, DEFAULT_MAX_RATING);
        }
        if (fields.contains(STATS)) {
            if (!sourceMap.containsKey(STATS) || sourceMap.get(STATS) == null) {
                sourceMap.put(STATS, new HashMap<>());
            }
            Object statsObj = sourceMap.get(STATS);
            if (statsObj instanceof Map) {
                Map<String, Object> stats = (Map<String, Object>) sourceMap.get(STATS);
                stats.putIfAbsent(COPILOT, DEFAULT_COPILOT_STATS);
                stats.putIfAbsent(DESIGN, DEFAULT_DESIGN_STATS);
                stats.putIfAbsent(DEVELOP, DEFAULT_DEVELOP_STATS);
                stats.putIfAbsent(DATA_SCIENCE, DEFAULT_DATA_SCIENCE_STATS);
            } else {
                LOGGER.error("Wrong stats class type {}", statsObj.getClass().getName());
            }
        }
    }

    /**
     * Get member search result support filter with handle and paging with offset and limit
     *
     * @param request
     *            member search result request
     * @param authUser
     *            Authentication user
     * @return MemberSearch Member search result
     */
    public QueryResult<List<Object>> searchMembers(MemberSearchRequest request, AuthUser authUser)
            throws SupplyException {

        // no need to check resource path
        boolean isMemberSearch = MEMBER_SEARCH.equals(request.getQuery());
        if (request.getOffset() == null) {
            request.setOffset(0);
        }
        if (request.getLimit() == null) {
            if (isMemberSearch) {
                request.setLimit(elasticsearchDefaultLimit == null ? DEFAULT_LIMIT : elasticsearchDefaultLimit);
            } else {
                request.setLimit(elasticsearchDefaultAdminLimit == null ? DEFAULT_ADMIN_LIMIT
                        : elasticsearchDefaultAdminLimit);
            }
        }


        // no need to check query
        if (isMemberSearch && request.getHandle() == null) {
            throw new SupplyException(MISSING_ERROR, HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(request.getOffset());
            searchSourceBuilder.size(request.getLimit());
            String[] includes = null;
            String[] excludes = null;
            FilterBuilder filterBuilder = null;
            QueryBuilder queryBuilder = null;
            if (isMemberSearch) {
                LOGGER.debug("Invoking member search query");
                // make sure handle is lowercase
                String handle = decodeURIComponent(request.getHandle()).toLowerCase();

                List<FilterBuilder> shouldFilters = new ArrayList<>();
                shouldFilters.add(FilterBuilders.existsFilter(PHOTO_URL));
                shouldFilters.add(FilterBuilders.existsFilter(DESCRIPTION));
                NestedFilterBuilder nestedFilterBuilder = FilterBuilders.nestedFilter(SKILLS, FilterBuilders
                        .existsFilter(SKILLS));
                // currently local docker container not work with _cache:true
                if (Boolean.TRUE.equals(elasticsearchCacheNestedFilter)) {
                    nestedFilterBuilder.cache(true);
                }
                shouldFilters.add(nestedFilterBuilder);

                // shouldFilters
                filterBuilder = FilterBuilders.boolFilter().should(shouldFilters.toArray(new FilterBuilder[0])

                ).must(FilterBuilders.termFilter(STATUS, ACTIVE_STATUS));

                queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.termQuery(HANDLE_PHRASE, handle)).should(
                        QueryBuilders.termQuery(HANDLE, handle));

                includes = MEMBER_SEARCH_INCLUDES;
                excludes = MEMBER_SEARCH_EXCLUDES;
            } else if (MEMBER_SKILL.equals(request.getQuery())) {
                LOGGER.debug("Invoking skill leaderboards query");

                // build query filter base on skills.name or skills.id
                // request's fields in format of termName:termValue
                String[] searchTerms = request.getFields().split(":");
                // searchTerms[0] is the term name to search
                // searchTerms[1] is the term value to search
                String termName = searchTerms[0];
                String termValue = searchTerms[1];
                FilterBuilder skillFilter = FilterBuilders.queryFilter(QueryBuilders.termQuery(termName, termValue));
                // search only active member
                filterBuilder = FilterBuilders.termFilter(STATUS, ACTIVE_STATUS);

                // sort by skills.score
                SortBuilder sortByScore = SortBuilders.fieldSort(SKILLS_SCORE).order(SortOrder.DESC).setNestedPath(
                        SKILLS).setNestedFilter(FilterBuilders.termFilter(termName, termValue));

                // build the search query
                queryBuilder = QueryBuilders.nestedQuery(SKILLS, skillFilter);
                searchSourceBuilder.sort(sortByScore);
                searchSourceBuilder.sort(WINS, SortOrder.DESC);
                includes = MEMBER_SEARCH_INCLUDES;
                excludes = MEMBER_SEARCH_EXCLUDES;
            } else {
                String queryString = request.getQuery();
                if (queryString == null) {
                    queryString = ALL_QUERY;
                }

                // convert + to space -
                queryString = decodeURIComponent(queryString).replace(PLUS, WHITESPACE);
                String fieldsQuery = request.getFields();
                if (fieldsQuery != null) {
                    includes = decodeURIComponent(fieldsQuery).split(COMMA);
                }

                if (request.getStatus() != null) {
                    filterBuilder = FilterBuilders.termFilter(STATUS, request.getStatus().toLowerCase());
                }

                queryBuilder = QueryBuilders.queryString(queryString);
                searchSourceBuilder.query(QueryBuilders.filteredQuery(queryBuilder, filterBuilder));
                excludes = isAdmin(authUser) ? SEARCH_EXCLUDES_WITHOUT_EMAIL : SEARCH_EXCLUDES;

                if (includes != null && authUser != null && authUser.isMachine()) {
                    if (Arrays.asList(includes).contains("email") && (authUser.getScope() == null || !authUser.getScope().contains("read:user_profiles"))) {
                        throw new SupplyException("The user can not search the profile with email", HttpServletResponse.SC_FORBIDDEN);
                    }
                    if (Arrays.asList(includes).contains("email") && authUser.getScope() != null && authUser.getScope().contains("read:user_profiles")) {
                        excludes = SEARCH_EXCLUDES_WITHOUT_EMAIL;
                    }
                }
            }
            searchSourceBuilder.query(QueryBuilders.filteredQuery(queryBuilder, filterBuilder));
            searchSourceBuilder.fetchSource(includes, excludes);
            final String query = searchSourceBuilder.toString();
            LOGGER.debug("query to search elasticsearch {} ", query);

            Search search = new Search.Builder(query).addIndex(Constants.MEMBER_INDEX_NAME).addType(
                    Constants.MEMBER_DOCUMENT_TYPE).build();

            SearchResult searchResult = client.execute(search);
            if (!searchResult.isSucceeded()) {
                final String errorMessage = searchResult.getErrorMessage();
                LOGGER.error("error to search elasticsearch {} ", errorMessage);
                throw new SupplyException(errorMessage, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            List<SearchResult.Hit<Object, Void>> resultList = searchResult.getHits(Object.class);
            List<Object> objects = new ArrayList<>();
            if (includes == null) {
                includes = DEFAULT_INCLUDES;
            }
            final List<String> includeFields = Arrays.asList(includes);
            for (SearchResult.Hit<Object, Void> hit : resultList) {
                Object source = hit.source;
                if (source instanceof Map) {
                    fixObject((Map<String, Object>) source, includeFields);
                }
                objects.add(source);
            }
            QueryResult<List<Object>> queryResult = new QueryResult<>();

            queryResult.setData(objects);
            queryResult.setRowCount(searchResult.getTotal());
            return queryResult;
        } catch (ClassCastException e) {
            LOGGER.error(ERROR_CAST, e);
            throw new SupplyException(ERROR_CAST, HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalFormatException e) {
            LOGGER.error(ERROR_QUERY, e);
            throw new SupplyException(ERROR_QUERY, HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            LOGGER.error(ERROR_SEARCH, e);
            throw new SupplyException(ERROR_SEARCH, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get leaderboards of Top 10 Members that matching the specified criteria.
     *
     * @param queryParam
     *            query parameter containing criteria
     * @param authUser
     *            Authentication user
     * @return leaderboards
     * @throws SupplyException
     *             if any error occurs while searching for members
     */
    public QueryResult<List<Object>> searchLeaderboards(QueryParameter queryParam, AuthUser authUser)
            throws SupplyException {
        FilterParameter filterParameter = queryParam.getFilter();
        String type = null;
        String name = null;
        String id = null;

        Set<String> filterParams = null;
        if (filterParameter.getFields() != null) {
            filterParams = filterParameter.getFields();
        }

        // Check whether filter contains invalid params
        for (String params : filterParams) {
            if (!(params.equals("type") || params.equals("name") || params.equals("id"))) {
                throw new IllegalArgumentException("Valid params are: type and id or name");
            }
        }

        // Check whether type is provided
        if (!filterParameter.contains("type")) {
            throw new IllegalArgumentException("filter param type is required");
        }

        // Check whether name or id is provided
        if (!filterParameter.contains("name") && !filterParameter.contains("id")) {
            throw new IllegalArgumentException("filter param name or id is required");
        }
        if (filterParameter.contains("name") && filterParameter.contains("id")) {
            throw new IllegalArgumentException("Both name and id are provided.");
        }
        if (filterParameter.contains("name")) {
            name = filterParameter.get("name").toString();
        } else if (filterParameter.contains("id")) {
            id = filterParameter.get("id").toString();
        }

        type = filterParameter.get("type").toString();
        if (!MEMBER_SKILL.equals(type.toUpperCase())) {
            throw new IllegalArgumentException("Currenty only MEMBER_SKILL is supported for type");
        }

        // Construct a MemberSearchRequest to search for members
        MemberSearchRequest searchRequest = new MemberSearchRequest();
        searchRequest.setOffset(0);
        searchRequest.setLimit(10); // Top 10

        searchRequest.setQuery(type.toUpperCase());

        // use "fields" field to hold the name or id parameter
        // in format of skills.name:value or skills.id:value
        if (name != null) {
            searchRequest.setFields(SKILLS_NAME + ":" + name.toLowerCase());
        } else {
            searchRequest.setFields(SKILLS_ID + ":" + id.toLowerCase());
        }

        LOGGER.debug("type: " + type + ", name: " + name + ", id: " + id);

        // delegate the real search
        return searchMembers(searchRequest, authUser);
    }
}