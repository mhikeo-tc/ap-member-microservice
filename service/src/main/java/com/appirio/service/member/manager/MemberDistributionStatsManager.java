package com.appirio.service.member.manager;

import com.appirio.service.member.api.MemberDistributionStats;
import com.appirio.service.member.dao.MemberDistributionStatsDAO;
import com.appirio.supply.constants.SubTrack;
import com.appirio.supply.constants.Track;
import com.appirio.tech.core.api.v3.request.FilterParameter;
import com.appirio.tech.core.api.v3.request.QueryParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Manager for Member distribution stats
 *
 * Created by rakeshrecharla on 8/20/15.
 */
public class MemberDistributionStatsManager {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(MemberDistributionStatsManager.class);

    /**
     * DAO for Member distribution stats
     */
    private MemberDistributionStatsDAO memberDistributionStatsDAO;

    /**
     * Constructor to initialize DAOs for member distribution stats
     * @param memberDistributionStatsDAO    Member distribution stats DAO
     */
    public MemberDistributionStatsManager(MemberDistributionStatsDAO memberDistributionStatsDAO) {
        this.memberDistributionStatsDAO = memberDistributionStatsDAO;
    }

    /**
     * Get member distribution statistics
     * @param  queryParam                   Query parameter
     * @return MemberDistributionStats      Member distribution statistics
     */
    public MemberDistributionStats getMemberDistributionStats(QueryParameter queryParam) {

        FilterParameter filterParameter = queryParam.getFilter();
        String track = null;
        String subTrack = null;

        Set<String> filterParams = null;
        if (filterParameter.getFields() != null) {
            filterParams = filterParameter.getFields();
        }

        for (String params : filterParams) {
            if (!params.equals("track") && !params.equals("subTrack")) {
                throw new IllegalArgumentException(String.format("Valid params are track and subTrack"));
            }
        }

        if (!filterParameter.contains("track")) {
            throw new IllegalArgumentException(String.format("filter param track is required"));
        }

        if (!filterParameter.contains("subTrack")) {
            throw new IllegalArgumentException(String.format("filter param subTrack is required"));
        }

        track = filterParameter.get("track").toString();
        subTrack = filterParameter.get("subTrack").toString();

        if (!Track.isValid(track.toUpperCase())) {
            throw new IllegalArgumentException(String.format("track %s is not valid", track));
        }

        if (!SubTrack.isValid(Enum.valueOf(Track.class, track.toUpperCase()), subTrack.toUpperCase())) {
            throw new IllegalArgumentException(String.format("subTrack %s is not valid", subTrack));
        }

        logger.debug("Track: " + track + " subTrack: " + subTrack);
        return memberDistributionStatsDAO.getMemberDistributionStats(track.toUpperCase(), subTrack.toUpperCase());
    }
}