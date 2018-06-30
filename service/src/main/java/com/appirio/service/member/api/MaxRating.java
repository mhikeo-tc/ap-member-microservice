package com.appirio.service.member.api;

import lombok.Setter;

import lombok.Getter;

/**
 * Represents the Max rating of the user across all stats
 *
 * @author mdesiderio@appirio.com
 *
 */
public class MaxRating {

	/**
	 * The highest current rating of the user
	 */
	@Getter
	@Setter
	private Long rating;
	
	/**
	 * The track in which the user has their current highest rating
	 */
	@Getter
	@Setter
	private String track;
	
	/**
	 * The subtrack in which the user has their current highest rating
	 */
	@Getter
	@Setter
	private String subTrack;
}