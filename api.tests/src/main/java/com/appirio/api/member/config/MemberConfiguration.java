package com.appirio.api.member.config;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.appirio.automation.api.config.Configuration;

public class MemberConfiguration extends Configuration{
	private static final String MEMBER_RETRIEVE_PROFILE   			= "retrieveMemberProfile";
	private static final String MEMBER_GENERATE_PHOTOURL   			= "postGeneratePhotoUrl";
	private static final String MEMBER_PUT_PROFILE 					= "putMemberProfile";
	private static final String MEMBER_PUT_PHOTOURL 					= "putMemberPhotoUrl";
	private static final String MEMBER_FINANCIAL 					= "retrieveMemberFinancial";
	private static final String MEMBER_STATS 						= "retrieveMemberStats";
	private static final String MEMBER_SKILLS						= "retrieveMemberSkills";
	private static final String MEMBER_COPILOT_FEEDBACK				= "retrieveCopilotFeedback";
	private static final String MEMBER_STATS_HISTORY					= "retrieveMemberStatsHistory";
	private static final String MEMBER_STATS_DISTRIBUTION			= "retrieveMemberStatsDistribution";
	private static final String MEMBER_GET_SKILLS_TAGS 			= "retrieveSkillTagsList";
	private static final String MEMBER_UPDATE_SKILLS 			= "updateMemberSkills";
	private static final String MEMBER_EXTERNAL_ACCOUNTS			= "retrieveExternalAccounts";
	private static final String MEMBER_GET_EXTERNAL_LINKS_DATA			= "getMemberExternalLinksData";
	private static final String MEMBER_UPDATE_EXTERNAL_LINK			= "updateMemberExternalLink";
	private static final String MEMBER_DELETE_EXTERNAL_LINK			= "deleteMemberExternalLink";
	private static final String GET_USER_DYNAMODB				= "retrieveUserDynamoDB";
	private static final String PROPS_MEMBER 						= "member.properties"; 
	
	private static PropertiesConfiguration memberPropertyConfig = null;

	private static MemberConfiguration memberConfiguration = null;

	private MemberConfiguration() {	   
	}

	public static MemberConfiguration initialize() {
		if(memberConfiguration == null) {  
			memberConfiguration = new MemberConfiguration();
			memberPropertyConfig = memberConfiguration.loadConfiguration(PROPS_MEMBER);
		}
		return memberConfiguration;
	}
	
	public static String getRetrieveMemberProfileEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_RETRIEVE_PROFILE);
	}
	
	public static String getGeneratePhotoUrlEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_GENERATE_PHOTOURL);
	}

	public static String getMemberPutProfileEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_PUT_PROFILE);
	}

	public static String getPutPhotoUrlEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_PUT_PHOTOURL);
	}

	public static String getMemberFinancialEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_FINANCIAL);
	}

	public static String getMemberStatsEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_STATS);
	}
	
	public static String getMemberSkillsEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_SKILLS);
	}
	
	public static String getCopilotFeedbackEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_COPILOT_FEEDBACK);
	}
	
	public static String getMemberStatsHistoryEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_STATS_HISTORY);
	}
	
	public static String getMemberStatsDistributionEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_STATS_DISTRIBUTION);
	}
	
	public static String getMemberSkillTagsEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_GET_SKILLS_TAGS);
	}
	
	public static String getMemberUpdateSkillsEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_UPDATE_SKILLS);
	}
	
	public static String getMemberExternalAccountsEndPoint() {
		return getValue(memberPropertyConfig, MEMBER_EXTERNAL_ACCOUNTS);
	}
	
	public static String getMemberGetExternalLinksData() {
		return getValue(memberPropertyConfig, MEMBER_GET_EXTERNAL_LINKS_DATA);
	}
	
	public static String getMemberUpdateExternalLink() {
		return getValue(memberPropertyConfig, MEMBER_UPDATE_EXTERNAL_LINK);
	}
	
	public static String getMemberDeleteExternalLink() {
		return getValue(memberPropertyConfig, MEMBER_DELETE_EXTERNAL_LINK);
	}
	
	//@todo- As of now hard coded the value of v2 api url for dynamoDB, but need to look for a solution for this
	public static String getUserDynamoDBEndPoint() {
		return getValue(memberPropertyConfig, GET_USER_DYNAMODB);
	}
}
