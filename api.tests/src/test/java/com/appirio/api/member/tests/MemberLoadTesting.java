package com.appirio.api.member.tests;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.appirio.api.member.config.MemberConfiguration;
import com.appirio.api.member.util.MemberUtil;
import com.appirio.automation.api.DefaultResponse;
import com.appirio.automation.api.config.AuthenticationConfiguration;
import com.appirio.automation.api.config.EnvironmentConfiguration;
import com.appirio.automation.api.config.UserConfiguration;
import com.appirio.automation.api.model.UserInfo;
import com.appirio.automation.api.service.UserService;
import com.appirio.automation.api.util.ApiUtil;
import com.fasterxml.jackson.databind.JsonNode;

public class MemberLoadTesting {
	final static Logger logger = Logger.getLogger(MemberLoadTesting.class);
	JSONObject jsonObjMain = null;
	JsonNode rootNode = null;
	final static UserService userService = new UserService();
	final static MemberUtil util = new MemberUtil();
	int iterationCount= 0;
	
	/**
	 * Initialise the configurations
	 * @throws Exception
	 */
	@BeforeClass
	public void setUp() throws Exception {
		logger.info("MemberLoadTesting:setUp:Entering set up phase to initialise the configurations.");
		EnvironmentConfiguration.initialize();
		AuthenticationConfiguration.initialize();
		MemberConfiguration.initialize();
		UserConfiguration.initialize();
		String jsonFile = util.getFile("member.json");
		rootNode = ApiUtil.readJsonFromFile(jsonFile);
	}
	

	/**
	 * Member microservice api endpoints not working for a new user, so holding off testing for a new user.
	 * Tests the api to get member profile by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 1, testName = "Get member profile by creating a new user using same user authentication header", 
			description = "Test get member profile")
	public void testGetMemberProfile() {
		logger.info("MemberTest:testGetMemberProfile:Testing 'Get member profile' api endpoint.");
		JsonNode userNode = rootNode.get("memberLoadCreate");
		JsonNode noOfUsers =rootNode.get("NoOfUsersToCreate");
		int userCount =Integer.parseInt(noOfUsers.path("param").path("userCount").textValue());
		JsonNode memberUpdateNode = rootNode.get("memberProfileUpdations");
		UserInfo userInfo=null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response=null;
		//Iterate over set of member parameters one by one
		for(int count = 0;count<userCount;count++) {
			userInfo = userService.createAnActivatedUser(userNode);
			String handle = userInfo.getUserName();
			headers = util.getHeader(handle,userInfo.getPassword());
			//Get profile of the new user created using the same user's auth token
			response = util.retrieveMemberProfile(handle, headers);
			logger.info("MemberTest:testGetMemberProfile:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//New user will not have this field 'otherLangName'
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response,"otherLangName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "firstName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "lastName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "email"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "addresses"),true);
			
			//Update member details
			response = util.updateMemberProfile(userInfo, memberUpdateNode);	
			logger.info("MemberTest:testGetMemberProfile:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//GET api call to check the member details after updation
			response = util.retrieveMemberProfile(handle, headers);
			logger.info("MemberTest:testGetMemberProfile:Response status for get request : "+response.getCode());
		    Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "otherLangName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "firstName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "lastName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "email"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "addresses"),true);
			iterationCount++;
			
		}	
		Assert.assertEquals(iterationCount, userCount);
		System.out.println("iterationCount="+iterationCount);
		System.out.println("userCount="+userCount);
	}
	
}
