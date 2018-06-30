package com.appirio.api.member.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.appirio.api.member.config.MemberConfiguration;
import com.appirio.api.member.model.User;
import com.appirio.api.member.util.MemberUtil;
import com.appirio.automation.api.DefaultRequestProcessor;
import com.appirio.automation.api.DefaultResponse;
import com.appirio.automation.api.config.AuthenticationConfiguration;
import com.appirio.automation.api.config.EnvironmentConfiguration;
import com.appirio.automation.api.config.UserConfiguration;
import com.appirio.automation.api.exception.DefaultRequestProcessorException;
import com.appirio.automation.api.model.UserInfo;
import com.appirio.automation.api.service.UserService;
import com.appirio.automation.api.util.ApiUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class containing tests to test APIs related to member microservice.
 * Author : Munmun Mathur
 * Created Date : 2015/08/04
 * Updated : 2015/08/07 Implement fixed api endpoints and review work
 * Updated : 2015/08/11 Refactored code as the api issues got fixed and 'getRequestJsonNode' method
 * 			 added in the automation lib class 'DefaultRequestProcessor'.
 * Updated : 2015/08/17 Applied automation lib code changes
 * @todo : Continues changes are going on in the api endpoints, as told by Rakesh. As a result, some of the api tests may fail.
 * 		   Might have to make changes in the tests once all the updations are done by dev.
 * Updated : 2015/09/01
 * 			Added tests for new api endpoints and some negative scenarios, and updated old api tests due to changes
 * 			in the microservice api endpoints. Disabled some tests for a new user as the api endpoints not working for newly created users.
 * Updated : 2015/09/10
 * 			Updated the api tests for ‘generate the photoUrl’ and ‘Put Member photo Url’ as there are changes in the api implementation, made changes as per the 
 * 			review comments by Rakesh(dev owner) and implemented 2 new api endpoints for member skills. Modified asserts for get member statistics api endpoint.
 * Updated : 2015/10/06
 * 			 Update JSON and refactor code for dynamic content.
 * 			 
 */
public class MemberTest {
 
	final static Logger logger = Logger.getLogger(MemberTest.class);
	JSONObject jsonObjMain = null;
	JsonNode rootNode = null;
	final static UserService userService = new UserService();
	final static MemberUtil util = new MemberUtil();
	int iterationCount= 0;
	JsonNode usersJson = null;
	Map<String,User> usersMap = null;
	private Map<String,UserInfo> newUsersMap = new HashMap<String,UserInfo>();
	/**
	 * Initialise the configurations
	 * @throws Exception
	 */
	@BeforeClass
	public void setUp() throws Exception {
		logger.info("MemberTest:setUp:Entering set up phase to initialise the configurations.");
		EnvironmentConfiguration.initialize();
		AuthenticationConfiguration.initialize();
		MemberConfiguration.initialize();
		UserConfiguration.initialize();
		String jsonFile = util.getFile("member.json");
		rootNode = ApiUtil.readJsonFromFile(jsonFile);
		usersJson = rootNode.get("userCredentials");
		usersMap = util.getUsers(usersJson);
		newUsersMap = util.createUsers(rootNode.path("memberCreate"));
		
	}
	
	
	/**
	 * Tests the api to get member profile by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 1, testName = "Get member profile by creating a new user using same user authentication header", 
			description = "Test get member profile")
	public void testGetMemberProfile() {
		logger.info("MemberTest:testGetMemberProfile:Testing 'Get member profile' api endpoint.");
		JsonNode memberUpdateNode = rootNode.get("memberProfileAddFieldsToUpdate");
		UserInfo userInfo=null;
		iterationCount=0;
		String handle;
		List<NameValuePair> headers = null;
		DefaultResponse response=null;
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
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
		Assert.assertEquals(iterationCount, newUsersMap.size());
	}
	
	/**
	 * Tests the api to get member profile by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 2, testName = "Get member profile by creating a new user without authentication header", 
			description = "Test get member profile")
	public void testGetMemberProfileNewUser() {
		logger.info("MemberTest:testGetMemberProfileNewUser:Testing 'Get member profile' api endpoint.");
		JsonNode memberUpdateNode = rootNode.get("memberProfileAddFieldsToUpdate");
		UserInfo userInfo=null;
		iterationCount=0;
		DefaultResponse response=null;
		String handle;
		//Iterate over set of member parameters one by one
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			//Update member details
			response = util.updateMemberProfile(userInfo, memberUpdateNode);
			logger.info("MemberTest:testGetMemberProfileNewUser:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			//Get profile of the new user created
			response = util.retrieveMemberProfile(handle, null);
			logger.info("MemberTest:testGetMemberProfileNewUser:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "otherLangName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "firstName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "lastName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "email"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "addresses"),false);
			iterationCount++;
		}
		Assert.assertEquals(iterationCount, newUsersMap.size());
	}

	/**
	 * Tests the api to get member profile by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 3, testName = "Get member profile of an existing user with some other user's jwt token", 
			description = "Test get member profile")
	public void testGetMemberProfileExistingUser() {
		logger.info("MemberTest:testGetMemberProfileExistingUser:Testing 'Get member profile' api endpoint.");
		JsonNode userNode = rootNode.get("memberProfile");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response= null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    String handle = jsonUserObject.path("param").path("handle").textValue();
			headers = util.getHeader();
			response = util.retrieveMemberProfile(handle, headers);
			logger.info("MemberTest:testGetMemberProfileExistingUser:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "otherLangName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "firstName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "lastName"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "email"),false);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "addresses"),false);
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to get member profile by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 4, testName = "Get member profile of an existing user with the same user's jwt token", 
			description = "Test get member profile")
	public void testGetMemberProfileExistingUserToken() {
		logger.info("MemberTest:testGetMemberProfileExistingUser:Testing 'Get member profile' api endpoint.");
		JsonNode userNode = rootNode.get("memberProfile");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String handle;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			handle = jsonUserObject.path("param").path("handle").textValue();
		    u = usersMap.get(handle);
			headers = util.getHeader(handle,u.getPassword());
			response = util.retrieveMemberProfile(handle, headers);
			logger.info("MemberTest:testGetMemberProfileExistingUserToken:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "otherLangName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "firstName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "lastName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "email"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "addresses"),true);
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to get member financial information by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 5, testName = "Get member financial information of an existing user", 
			description = "Test get member financial information")
	public void testGetMemberFinancialInfoExistingUserToken() {
		logger.info("MemberTest:testGetMemberFinancialInfoExistingUserToken:Testing 'Get member financial information' api endpoint.");
		JsonNode userNode = rootNode.get("memberFinancial");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		String retrieveMemberFinancialInfo;
		DefaultResponse response = null;
		String handle;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			retrieveMemberFinancialInfo = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberFinancialEndPoint();
		    handle = jsonUserObject.path("param").path("handle").textValue();
			retrieveMemberFinancialInfo = ApiUtil.replaceToken(retrieveMemberFinancialInfo,"@handle",handle);
			u = usersMap.get(handle);
			headers = util.getHeader(handle,u.getPassword());
		    response=DefaultRequestProcessor.getRequest(retrieveMemberFinancialInfo, null, headers);
		    logger.info("MemberTest:testGetMemberFinancialInfoExistingUserToken:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject=response.getResponseData();
			Assert.assertEquals(jsonObject.path("result").path("content").isArray(), true);
			Assert.assertEquals(jsonObject.path("result").path("content").size()>=0,true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "amount"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "userId"),true);
			((ObjectNode)jsonUserObject.path("param")).remove("handle");
			if(jsonUserObject.path("param").fields().hasNext())
				Assert.assertTrue(ApiUtil.assertQueryResponse(response,jsonUserObject.path("param")));
			
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to get member statistics information by passing user handle and field values as parameters and
	 * JWT token as header 
	 */
	@Test(priority = 6, testName = "Get member statistics information of an existing user", 
			description = "Test get member statistics information")
	public void testGetMemberStatisticsInfo() {
		logger.info("MemberTest:testGetMemberStatisticsInfoExistingUserToken:Testing 'Get member statistics information' api endpoint.");
		JsonNode userNode = rootNode.get("memberStatistics");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> urlParameters=new ArrayList<NameValuePair>();
		DefaultResponse response=null;
		String retrieveMemberStatisticsInfo;
		String statsType = null;
		String handle;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    retrieveMemberStatisticsInfo = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberStatsEndPoint();
			urlParameters=util.constructParameters(jsonUserObject.get("param"));
			handle = jsonUserObject.path("param").path("handle").textValue();
			retrieveMemberStatisticsInfo = ApiUtil.replaceToken(retrieveMemberStatisticsInfo,"@handle",handle);
		    response=DefaultRequestProcessor.getRequest(retrieveMemberStatisticsInfo, urlParameters, null);
		    logger.info("MemberTest:testGetMemberStatisticsInfo:Response status for get request : "+response.getCode());
		    if(jsonUserObject.get("param").has("fields"))
			  statsType= jsonUserObject.get("param").get("fields").toString();
			Assert.assertEquals(response.getCode(), 200);
			if(statsType!=null){
				if(statsType.contains("develop")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DEVELOP"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DEVELOP"),false);
				
				if(statsType.contains("design")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DESIGN"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DESIGN"),false);
				
				if(statsType.contains("copilot")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "COPILOT"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "COPILOT"),false);
				
				if(statsType.contains("data_science")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DATA_SCIENCE"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DATA_SCIENCE"),false);
			}
			else{
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DEVELOP"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DESIGN"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "COPILOT"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "DATA_SCIENCE"),true);
			}
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}

	/**
	 * Tests the api to get member skills information by passing user handle as parameter and
	 * JWT token as header 
	 **/
	@Test(priority = 7, testName = "Get member skills information of an existing user", 
			description = "Test get member skills information")
	public void testGetMemberSkillsInfo() {
		logger.info("MemberTest:testGetMemberSkillsInfoExistingUserToken:Testing 'Get member skills information' api endpoint.");
		JsonNode userNode = rootNode.get("memberSkills");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response= null;
		String retrieveMemberSkillsInfo;
		String handle;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    retrieveMemberSkillsInfo = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberSkillsEndPoint();
			handle = jsonUserObject.path("param").path("handle").textValue();
			retrieveMemberSkillsInfo = ApiUtil.replaceToken(retrieveMemberSkillsInfo,"@handle",handle);
			headers = util.getHeader();
		    response=DefaultRequestProcessor.getRequest(retrieveMemberSkillsInfo, null, headers);
		    logger.info("MemberTest:testGetMemberSkillsInfo:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject = response.getResponseData();
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "skills"),true);
			Assert.assertEquals(jsonObject.path("result").path("content").path("skills").size()>=0,true);
			JsonNode skills = jsonObject.path("result").path("content").path("skills");
			Assert.assertTrue(util.assertFieldsInMemberSkillSet(skills, handle));
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to get member copilot feedback by passing user handle as parameter and
	 * JWT token as header 
	 */
	@Test(priority = 8, testName = "Get member copilot feedback of an existing user", 
			description = "Test get member copilot feedback of an existing user")
	public void testGetMemberCopilotFeedback() {
		logger.info("MemberTest:testGetMemberCopilotFeedback:Testing 'Get member copilot feedback' api endpoint.");
		JsonNode userNode = rootNode.get("copilotFeedback");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		String retrieveMemberCopilotFeedback;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response=null;
		String handle;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    retrieveMemberCopilotFeedback = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getCopilotFeedbackEndPoint();
			handle = jsonUserObject.path("param").path("handle").textValue();
			User u = usersMap.get(handle);
			retrieveMemberCopilotFeedback = ApiUtil.replaceToken(retrieveMemberCopilotFeedback,"@handle",handle);
			headers = util.getHeader();
		    response=DefaultRequestProcessor.getRequest(retrieveMemberCopilotFeedback, null, headers);
		    logger.info("MemberTest:testGetMemberCopilotFeedback:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject = response.getResponseData();
			Assert.assertEquals(jsonObject.path("result").path("content").isArray(), true);
			Assert.assertEquals(jsonObject.path("result").path("content").size()>=0,true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "reviewText"),true);
			((ObjectNode)jsonUserObject.path("param")).remove("handle");
			((ObjectNode)jsonUserObject.path("param")).put("userId",u.getUserId());
			Assert.assertTrue(ApiUtil.assertQueryResponse(response,jsonUserObject.path("param")));
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to update member profile by passing user details as json data and
	 * JWT token as header 
	 */
	
	//500 error :"result":{"success":true,"status":500,"metadata":null,"content":"Couldn't invoke public java.lang.Long 
	//com.appirio.service.member.api.MemberProfile.getCreatedAtDD()"},"version":"v3"}
	@Test(priority = 9, testName = "Create a new user and update member profile using the put request", 
			description = "Test put member profile")
	public void tesPutMemberProfile() {
		logger.info("MemberTest:testGetMemberProfileNewUser:Testing 'Put member profile' api endpoint.");
		JsonNode memberUpdateNode = rootNode.get("memberProfileAddFieldsToUpdate");
		UserInfo userInfo=null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String handle;
		//Iterate over set of member parameters one by one
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			headers = util.getHeader(handle,userInfo.getPassword());
			
			//Update member details
			response = util.updateMemberProfile(userInfo, memberUpdateNode);
			logger.info("MemberTest:tesPutMemberProfile:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//GET api call to get the member details and check the updated fields
			response=util.retrieveMemberProfile(handle, headers);
			logger.info("MemberTest:tesPutMemberProfile:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "otherLangName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "firstName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "lastName"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "email"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "addresses"),true);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "tracks"),true);
			iterationCount++;
		}
		Assert.assertEquals(iterationCount, newUsersMap.size());
	}
	
	/**
	 * Tests the api to update member profile of existing user by passing user details as json data and
	 * JWT token as header 
	 */
	@Test(priority = 10, testName = "Update member profile using the put request", 
			description = "Test put member profile")
	public void testPutMemberProfileExistingUser() {
		logger.info("MemberTest:testPutMemberProfileExistingUser:Testing 'Put member profile' api endpoint.");
		JsonNode userNode = rootNode.get("memberProfileUpdate");
		JsonNode memberUpdateNode = rootNode.get("memberProfileAddFieldsToUpdate");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String handle = null;
		String putMemberProfile;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    handle = jsonUserObject.path("param").path("handle").textValue();
			u = usersMap.get(handle.toLowerCase());
			headers = util.getHeader(handle,u.getPassword());
			
			//Update member details
			memberUpdateNode = util.addUpdatedFieldsToMemberObj(jsonUserObject, memberUpdateNode);
		    putMemberProfile = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberPutProfileEndPoint();
			putMemberProfile= ApiUtil.replaceToken(putMemberProfile,"@handle",handle);
			response = DefaultRequestProcessor.putRequest(putMemberProfile, null, headers,memberUpdateNode.toString());
			logger.info("MemberTest:testPutMemberProfileExistingUser:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//GET api call to get the member details and check the updated fields
			response=util.retrieveMemberProfile(handle, headers);
			logger.info("MemberTest:testPutMemberProfileExistingUser:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertTrue(ApiUtil.assertQueryResponse(response, memberUpdateNode.path("param")));
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests that a user is allowed to update his / her own profile only, using the update member profile api endpoint.
	*/
	@Test(priority = 11, testName = "Update member profile using the put request by some other user logged in", 
			description = "Tests that a user is allowed to update his / her own profile only, using the update member profile api endpoint."
			,expectedExceptions=DefaultRequestProcessorException.class)
	public void testPutMemberProfileAnotherUser() {
		logger.info("MemberTest:testPutMemberProfileAnotherUser:Testing 'Put member profile' api endpoint.");
		JsonNode userNode = rootNode.get("memberProfileUpdate");
		JsonNode memberUpdateNode = rootNode.get("memberProfileAddFieldsToUpdate");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String handle = null;
		String putMemberProfile;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    handle = jsonUserObject.path("param").path("handle").textValue();
		    //Auth token of some other user is used as header
			headers = util.getHeader();
			//Update member details
			memberUpdateNode = util.addUpdatedFieldsToMemberObj(jsonUserObject, memberUpdateNode);
		    putMemberProfile = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberPutProfileEndPoint();
			putMemberProfile= ApiUtil.replaceToken(putMemberProfile,"@handle",handle);
			response = DefaultRequestProcessor.putRequest(putMemberProfile, null, headers,memberUpdateNode.toString());
			logger.info("MemberTest:testPutMemberProfileAnotherUser:Response status for put request : "+response.getCode());
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	
	/**
	 * Tests the api to generate photo url by passing user handle as parameter and
	 * JWT token as header, and then putting the photo url to the member profile
	 */
	@Test(priority = 12, testName = "Generate and put the photo url of a new user", 
			description = "Test generate and put the photo url of a new user")
	public void testPostGeneratePhotoUrl() {
		
		logger.info("MemberTest:testGeneratePhotoUrl:Testing 'Generate and put the photo url of a new user' api endpoint.");
		JsonNode defaultParamGeneratePhotoUrl = rootNode.get("defaultParamGeneratePhotoUrl");
		JsonNode photoObject = null;
		iterationCount=0;
		UserInfo userInfo=null;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String generatePhotoUrl;
		String putPhotoUrl;
		String token;
		String handle;
		JsonNode responseData;
		//Iterate over set of member parameters one by one
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			generatePhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getGeneratePhotoUrlEndPoint();
			putPhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getPutPhotoUrlEndPoint();
			headers = util.getHeader(handle,userInfo.getPassword());
			
			//GET api call to get the member details and check that photoURL field is null
		    response=util.retrieveMemberProfile(handle, headers);
		    logger.info("MemberTest:testGeneratePhotoUrl:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response,"photo"),false);
			
			//POST api call to generate photo url
			generatePhotoUrl = ApiUtil.replaceToken(generatePhotoUrl,"@handle",handle);
			response = DefaultRequestProcessor.postRequest(generatePhotoUrl, null, headers,defaultParamGeneratePhotoUrl.toString());
			logger.info("MemberTest:testGeneratePhotoUrl:Response status for post request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			responseData = response.getResponseData();
			token=responseData.path("result").path("content").get("token").toString();
			Assert.assertNotEquals(token, "");
			Assert.assertNotNull(token);
			
			//Add the value of the token generated to the json photo object
			photoObject = rootNode.get("memberPutPhotoUrl");
			((ObjectNode)photoObject.path("param")).put("token",token);
			putPhotoUrl = ApiUtil.replaceToken(putPhotoUrl,"@handle",handle);
			//PUT api call to put photo url value
			response = DefaultRequestProcessor.putRequest(putPhotoUrl, null, headers, photoObject.toString());
			logger.info("MemberTest:testGeneratePhotoUrl:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//GET api call to get the member details and check that photoURL field is not null now
			response=util.retrieveMemberProfile(handle, headers);
			responseData = response.getResponseData();
			Assert.assertEquals(response.getCode(), 200);
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "photoURL"),true);
			logger.info("MemberTest:testGeneratePhotoUrl:Response status for get request : "+response.getCode());
			//Check that the value of field 'photoURL' in the response contains the token generated
			Assert.assertTrue(responseData.path("result").path("content").get("photoURL").toString().contains(token));
			iterationCount++;
		}

		Assert.assertEquals(iterationCount, newUsersMap.size());
	}
	
	/**
	 * Tests 2 api endoints for an existing user :
	 * - Api to generate photo url by passing user handle as parameter
	 * - Api to put the photo url to the member profile
	 */
	@Test(priority = 13, testName = "Generate and put the photo url of an existing user", 
			description = "Test generate and put the photo url of an existing user")
	public void testPostGeneratePhotoUrlExistingUser() {
		
		logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Testing 'Generate and put the photo url of an existing user' api endpoint.");
		JsonNode userNode = rootNode.get("memberGeneratePhotoUrl");
		JsonNode defaultParamGeneratePhotoUrl = rootNode.get("defaultParamGeneratePhotoUrl");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		JsonNode responseData = null;
		JsonNode photoObject=null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String generatePhotoUrl;
		String putPhotoUrl;
		String handle;
		String token;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			generatePhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getGeneratePhotoUrlEndPoint();
			putPhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getPutPhotoUrlEndPoint();
			
			handle = jsonUserObject.path("param").path("handle").textValue();
			u = usersMap.get(handle.toLowerCase());
			headers = util.getHeader(handle,u.getPassword());

			//POST api call to generate photo url
			generatePhotoUrl = ApiUtil.replaceToken(generatePhotoUrl,"@handle",handle);
			response = DefaultRequestProcessor.postRequest(generatePhotoUrl, null, headers,defaultParamGeneratePhotoUrl.toString());
			logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Response status for post request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject = response.getResponseData();
			token=jsonObject.path("result").path("content").get("token").toString();
			Assert.assertNotNull(token);
			Assert.assertNotEquals(token, "");
			
			photoObject = rootNode.get("memberPutPhotoUrl");
			((ObjectNode)photoObject.path("param")).put("token",token);
			putPhotoUrl = ApiUtil.replaceToken(putPhotoUrl,"@handle",handle);
			//PUT api call to put photo url value
			response = DefaultRequestProcessor.putRequest(putPhotoUrl, null, headers, photoObject.toString());
			logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//GET api call to get the member details and check that photoURL field is not null now
			response=util.retrieveMemberProfile(handle, headers);
			Assert.assertEquals(response.getCode(), 200);
			responseData = response.getResponseData();
			
			//Check that the response contains the field 'photoURL'
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "photoURL"),true);
			
			//Check that the value of field 'photoURL' in the response contains the token generated
			Assert.assertTrue(responseData.path("result").path("content").get("photoURL").toString().contains(token));
			logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Response status for get request : "+response.getCode());
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests that a user is allowed to generate his / her own photo url only, using the generate the photo ur api endpoint.
	 */
	@Test(priority = 14, testName = "Generate the photo url of an existing user by some other user logged in", 
			description = "Test generate the photo url of an existing user by some other user logged in",
			expectedExceptions=DefaultRequestProcessorException.class)
	public void testPostGeneratePhotoUrlOtherUser() {
		
		logger.info("MemberTest:testPostGeneratePhotoUrlOtherUser:Testing 'Generate the photo url of an existing user' api endpoint.");
		JsonNode userNode = rootNode.get("memberGeneratePhotoUrl");
		JsonNode defaultParamGeneratePhotoUrl = rootNode.get("defaultParamGeneratePhotoUrl");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String generatePhotoUrl;
		String handle;
		
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			generatePhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getGeneratePhotoUrlEndPoint();
			handle = jsonUserObject.path("param").path("handle").textValue();
			//Auth token of some other user is used as header
			headers = util.getHeader();

			//POST api call to generate photo url
			generatePhotoUrl = ApiUtil.replaceToken(generatePhotoUrl,"@handle",handle);
			response = DefaultRequestProcessor.postRequest(generatePhotoUrl, null, headers,defaultParamGeneratePhotoUrl.toString());
			logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Response status for post request : "+response.getCode());
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests that a user is allowed to update his / her own photo url only, using the generate the photo ur api endpoint.
	 */
	@Test(priority = 15, testName = "Put the photo url of an existing user by some other logged in user ", 
			description = "Test put the photo url of an existing user by some other logged in user",
			expectedExceptions=DefaultRequestProcessorException.class)
	public void testPutPhotoUrlOtherUser() {
		
		logger.info("MemberTest:testPostGeneratePhotoUrlOtherUser:Testing 'Generate the photo url of an existing user' api endpoint.");
		JsonNode userNode = rootNode.get("memberGeneratePhotoUrl");
		JsonNode defaultParamGeneratePhotoUrl = rootNode.get("defaultParamGeneratePhotoUrl");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String generatePhotoUrl;
		String putPhotoUrl;
		String handle;
		String token;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			generatePhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getGeneratePhotoUrlEndPoint();
			putPhotoUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getPutPhotoUrlEndPoint();
			handle = jsonUserObject.path("param").path("handle").textValue();
			//Auth token of same other user is used as header to generate photo url and get token
			u = usersMap.get(handle.toLowerCase());
			headers = util.getHeader(handle,u.getPassword());
			
			//POST api call to generate photo url
			generatePhotoUrl = ApiUtil.replaceToken(generatePhotoUrl,"@handle",handle);
			response = DefaultRequestProcessor.postRequest(generatePhotoUrl, null, headers,defaultParamGeneratePhotoUrl.toString());
			logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Response status for post request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject = response.getResponseData();
			token=jsonObject.path("result").path("content").get("token").toString();
			Assert.assertNotEquals(token, "");
			Assert.assertNotNull(token);
			
			JsonNode jsonPhotoObject = rootNode.get("memberPutPhotoUrl");
			((ObjectNode)jsonPhotoObject.path("param")).put("token",token);
			putPhotoUrl = ApiUtil.replaceToken(putPhotoUrl,"@handle",handle);
			//Auth token of some other user is used as header
			headers = util.getHeader();
			//PUT api call to put photo url value
			response = DefaultRequestProcessor.putRequest(putPhotoUrl, null, headers, jsonPhotoObject.toString());
			logger.info("MemberTest:testPostGeneratePhotoUrlExistingUser:Response status for put request : "+response.getCode());
			
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to get member statistics history by passing user handle and
	 * JWT token as header 
	 */
	@Test(priority = 16, testName = "Get member statistics history of an existing user", 
			description = "Test get member statistics history")
	public void testGetMemberStatsHistory() {
		logger.info("MemberTest:testGetMemberStatsHistory:ExistingUserToken:Testing 'Get member statistics history' api endpoint.");
		JsonNode userNode = rootNode.get("memberStatsHistory");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		int statsHistory=0;
		List<NameValuePair> headers = null;
		DefaultResponse response=null;
		String retrieveMemberStatisticsHistory;
		Iterator<JsonNode> developStatsHistory= null;
		JsonNode statshistoryDev = null;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    retrieveMemberStatisticsHistory = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberStatsHistoryEndPoint();
			String handle = jsonUserObject.path("param").path("handle").textValue();
			retrieveMemberStatisticsHistory = ApiUtil.replaceToken(retrieveMemberStatisticsHistory,"@handle",handle);
			u = usersMap.get(handle.toLowerCase());
			headers = util.getHeader(handle,u.getPassword());
			response=DefaultRequestProcessor.getRequest(retrieveMemberStatisticsHistory, null, headers);
		    logger.info("MemberTest:testGetMemberStatsHistory:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject = response.getResponseData();
			Assert.assertTrue(ApiUtil.assertFieldInQueryResponse(response, "handle"));
			Assert.assertEquals(jsonObject.path("result").get("content").path("handle").textValue().toLowerCase(), handle.toLowerCase());
			
			//Check for 'Develop' type stats history
			Assert.assertTrue(jsonObject.path("result").get("content").path("DEVELOP").path("subTracks").isArray());
		    statsHistory = jsonObject.path("result").get("content").path("DEVELOP").path("subTracks").size();
			Assert.assertNotNull(statsHistory);
			Assert.assertEquals(statsHistory>=0,true);
			developStatsHistory = jsonObject.path("result").get("content").path("DEVELOP").path("subTracks").elements();
			while(developStatsHistory.hasNext()) {
				statshistoryDev = developStatsHistory.next();
				Assert.assertTrue(statshistoryDev.path("history").isArray());
				Assert.assertEquals(statshistoryDev.path("history").size()>=0,true);
			}
			
			//Check for 'DATA_SCIENCE' type stats history
			developStatsHistory = jsonObject.path("result").get("content").path("DATA_SCIENCE").elements();
			while(developStatsHistory.hasNext()) {
				statshistoryDev = developStatsHistory.next();
				Assert.assertTrue(statshistoryDev.path("history").isArray());
				statsHistory = statshistoryDev.path("history").size();
				Assert.assertNotNull(statsHistory);
				Assert.assertEquals(statsHistory>=0,true);
			}
	
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}

	/**
	 * Tests the api to get member statistics distribution
	 */
	@Test(priority = 17, testName = "Get member distribution statistics of an existing user", 
			description = "Test get member distribution statistics")
	public void testGetMemberStatsDistribution() {
		logger.info("MemberTest:testGetMemberStatsDistribution:ExistingUserToken:Testing 'Get member distribution statistics' api endpoint.");
		JsonNode userNode = rootNode.get("memberStatsDistribution");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		int statsDistribution=0;
		List<NameValuePair> urlParameters = null;
		DefaultResponse response=null;
		String retrieveMemberStatisticsHistory;

		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    retrieveMemberStatisticsHistory = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberStatsDistributionEndPoint();
			urlParameters=ApiUtil.constructParameters(jsonUserObject.get("param"));
			response=DefaultRequestProcessor.getRequest(retrieveMemberStatisticsHistory, urlParameters, null);
		    logger.info("MemberTest:testGetMemberStatsHistory:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			JsonNode jsonObject = response.getResponseData();
			Assert.assertTrue(ApiUtil.assertQueryResponse(response,jsonUserObject.path("param").path("filter")));
			Assert.assertNotNull(jsonObject.path("result").get("content").get("distribution").toString());
			statsDistribution = jsonObject.path("result").get("content").path("distribution").size();
			Assert.assertNotNull(statsDistribution);
			Assert.assertEquals(statsDistribution>=0,true);
	
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	@Test(priority = 18, testName = "Update member skills of an existing user", 
			description = "Test update member skills")
	public void testUpdateMemberSkills() {
		logger.info("MemberTest:testUpdateMemberSkills:Testing 'Update member skills' api endpoint.");
		JsonNode userNode = rootNode.get("memberSkills");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response= null;
		String retrieveMemberSkillsTags = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberSkillTagsEndPoint();
		response = DefaultRequestProcessor.getRequest(retrieveMemberSkillsTags,null,null);
		List<JsonNode> tagsList = response.getResponseContents();
		String updateMemberSkills;
		JsonNode responseObject;
		String handle;
		JsonNode skillsSet = rootNode.get("updateMemberSkills");	
		String tagId1;
		String tagId2;
		int tagIdCount = 0;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			//Add skills
			tagId1 = tagsList.get(tagIdCount).path("id").toString();
			tagId2 = tagsList.get(++tagIdCount).path("id").toString();
			util.addSkillTagIds((ObjectNode)skillsSet.path("param").get("skills"),tagId1,"false");
		    util.addSkillTagIds((ObjectNode)skillsSet.path("param").get("skills"),tagId2,"true");
		    logger.info("MemberTest:testUpdateMemberSkills:Skills set after adding new skill tag ids : "+skillsSet);
			
			jsonUserObject = elements.next();
			updateMemberSkills = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberSkillsEndPoint();
			handle = jsonUserObject.path("param").path("handle").textValue();
			updateMemberSkills = ApiUtil.replaceToken(updateMemberSkills,"@handle",handle);
			u = usersMap.get(handle.toLowerCase());
			headers = util.getHeader(handle,u.getPassword());
			//headers = util.getHeader(handle,jsonUserObject.path("param").path("password").textValue());
		    response=DefaultRequestProcessor.patchRequest(updateMemberSkills, null, headers,skillsSet.toString());
		    logger.info("MemberTest:testUpdateMemberSkills:Response status for patch request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			responseObject = response.getResponseData();
			Assert.assertEquals(ApiUtil.assertFieldInQueryResponse(response, "skills"),true);
			Assert.assertEquals(responseObject.path("result").path("content").path("skills").size()>=0,true);
			JsonNode skills = responseObject.path("result").path("content").path("skills");
			Assert.assertNull(skills.get(tagId2));
			Assert.assertNotNull(skills.get(tagId1));
			//Remove added tagId nodes to clear skillsSet node for next user
			((ObjectNode)skillsSet.path("param").get("skills")).remove(tagId1);
			((ObjectNode)skillsSet.path("param").get("skills")).remove(tagId2);
			tagIdCount++;
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}

	
	@Test(priority = 19, testName = "Update member skills of a non logged in user", 
			description = "Test update member skills of a non logged in user",expectedExceptions=DefaultRequestProcessorException.class)
	public void testUpdateMemberSkillsOfNonLoggedInUser() {
		logger.info("MemberTest:testUpdateMemberSkills:Testing 'Update member skills' api endpoint for a non logged in user");
		JsonNode userNode = rootNode.get("memberSkills");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response= null;
		String retrieveMemberSkillsTags = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberSkillTagsEndPoint();
		response = DefaultRequestProcessor.getRequest(retrieveMemberSkillsTags,null,null);
		List<JsonNode> tagsList = response.getResponseContents();
		String updateMemberSkills;
		String handle;
		JsonNode skillsSet = rootNode.get("updateMemberSkills");	
		String tagId1;
		String tagId2;
		int tagIdCount = 0;
		
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			//Add skills
			tagId1 = tagsList.get(tagIdCount).path("id").toString();
			tagId2 = tagsList.get(++tagIdCount).path("id").toString();
			util.addSkillTagIds((ObjectNode)skillsSet.path("param").get("skills"),tagId1,"false");
		    util.addSkillTagIds((ObjectNode)skillsSet.path("param").get("skills"),tagId2,"true");
		    logger.info("MemberTest:testUpdateMemberSkills:Skills set after adding new skill tag ids : "+skillsSet);
			
			jsonUserObject = elements.next();
			updateMemberSkills = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberSkillsEndPoint();
			handle = jsonUserObject.path("param").path("handle").textValue();
			updateMemberSkills = ApiUtil.replaceToken(updateMemberSkills,"@handle",handle);
			headers = util.getHeader();
		    response=DefaultRequestProcessor.patchRequest(updateMemberSkills, null, headers,skillsSet.toString());
		    logger.info("MemberTest:testUpdateMemberSkills:Response status for patch request : "+response.getCode());
			//Remove added tagId nodes to clear skillsSet node for next user
			((ObjectNode)skillsSet.path("param").get("skills")).remove(tagId1);
			((ObjectNode)skillsSet.path("param").get("skills")).remove(tagId2);
			tagIdCount++;
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}

	/**
	 * Tests the api to get member profile of an inactive user by passing user handle as parameter and
	 * JWT token(other user) as header 
	 */
	@Test(priority = 20, testName = "Get member profile of a newly created user but not activating it and using the jwt token of some other user", 
			description = "Test get member profile",expectedExceptions=DefaultRequestProcessorException.class)
	public void testGetMemberProfileInactiveUser() {
		logger.info("MemberTest:testGetMemberProfileInactiveUser:Testing 'Get member profile' api endpoint for inactive user.");
		JsonNode userNode = rootNode.get("memberCreate");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		UserInfo userInfo=null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response=null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			userInfo = userService.createUser(jsonUserObject);
			String handle = userInfo.getUserName();
			headers = util.getHeader();
			//Get profile of the new user created but not activated
			response = util.retrieveMemberProfile(handle, headers);
			iterationCount++;
			
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the api to get member external accounts information by passing user handle and field values as parameters and
	 * JWT token as header 
	 */
	@Test(priority = 21, testName = "Get member external accounts information of an existing user", 
			description = "Test get member external accounts information")
	public void testGetMemberExternalAccounts() {
		logger.info("MemberTest:testGetMemberExternalAccounts:Testing 'Get member external accounts' api endpoint.");
		JsonNode userNode = rootNode.get("memberExternalAccounts");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> urlParameters=new ArrayList<NameValuePair>();
		DefaultResponse response=null;
		String retrieveMemberExternalAccounts;
		String statsType = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			retrieveMemberExternalAccounts = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberExternalAccountsEndPoint();
			urlParameters=util.constructParameters(jsonUserObject.get("param"));
			String handle = jsonUserObject.path("param").path("handle").textValue();
			retrieveMemberExternalAccounts = ApiUtil.replaceToken(retrieveMemberExternalAccounts,"@handle",handle);
		    response=DefaultRequestProcessor.getRequest(retrieveMemberExternalAccounts, urlParameters, null);
		    logger.info("MemberTest:testGetMemberExternalAccounts:Response status for get request : "+response.getCode());
		    if(jsonUserObject.get("param").has("fields"))
			  statsType= jsonUserObject.get("param").get("fields").toString();
			Assert.assertEquals(response.getCode(), 200);
			if(statsType!=null){
				if(statsType.contains("behance")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "behance"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "behance"),false);
				
				if(statsType.contains("bitBucket")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "bitBucket"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "bitBucket"),false);
				
				if(statsType.contains("dribble")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "dribble"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "dribble"),false);
				
				if(statsType.contains("github")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "github"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "github"),false);
				
				if(statsType.contains("linkedIn")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "linkedIn"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "linkedIn"),false);
				
				if(statsType.contains("stackOverflow")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "stackOverflow"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "stackOverflow"),false);
				
				if(statsType.contains("twitter")) {
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "twitter"),true);
				} else
					Assert.assertEquals(util.assertFieldsAsFilterParams(response, "twitter"),false);
			}
			else{
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "behance"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "bitBucket"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "dribble"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "github"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "linkedIn"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "stackOverflow"),true);
				Assert.assertEquals(util.assertFieldsAsFilterParams(response, "twitter"),true);
			}
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}

	/**
	 * Tests that when a member profile is updated using the put request (v3) on informix, the updates are reflected on v2/users on dynamoDB.
	 */
	@Test(priority = 22, testName = "Update member profile using the put request (v3) on informix and check the updates are reflected on v2/users on dynamoDB.", 
			description = "Test that when a member profile is updated, the changes are reflected in the dynamoDB(v2)")
	public void testCheckMemberProfileUpdatesOnDynamoDB() {
		logger.info("MemberTest:testCheckMemberProfileUpdatesOnDynamoDB:Testing 'Put member profile' api endpoint.");
		JsonNode userNode = rootNode.get("memberProfileUpdate");
		JsonNode memberUpdateNode = rootNode.get("memberProfileAddFieldsToUpdate");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		List<NameValuePair> headers = null;
		DefaultResponse response = null;
		String handle = null;
		String putMemberProfile;
		User u = null;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    handle = jsonUserObject.path("param").path("handle").textValue();
		    u = usersMap.get(handle.toLowerCase());
			headers = util.getHeader(handle,u.getPassword());
			//Update member details
			memberUpdateNode = util.addUpdatedFieldsToMemberObj(jsonUserObject, memberUpdateNode);
		    putMemberProfile = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberPutProfileEndPoint();
			putMemberProfile= ApiUtil.replaceToken(putMemberProfile,"@handle",handle);
			response = DefaultRequestProcessor.putRequest(putMemberProfile, null, headers,memberUpdateNode.toString());
			logger.info("MemberTest:testPutMemberProfileExistingUser:Response status for put request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			
			//GET api call to get the member details using v2/users/ on dynamoDB and check the updated fields
			String retrieveMemberProfile = MemberConfiguration.getUserDynamoDBEndPoint();
			retrieveMemberProfile = ApiUtil.replaceToken(retrieveMemberProfile,"@handle",handle);
			//Get v2 auth token as header
			headers = util.getHeaderV2();
			response=DefaultRequestProcessor.getRequest(retrieveMemberProfile, null, headers);
			logger.info("MemberTest:testPutMemberProfileExistingUser:Response status for get request : "+response.getCode());
			Assert.assertEquals(response.getCode(), 200);
			/******Added assert to check value of field 'quote' only it is the only common field. Will add some asserts later after
			 discussing with Rakesh*******/
			JsonNode updatedMemberProfile = response.getResponseData();
			Assert.assertEquals(updatedMemberProfile.path("quote").textValue(), memberUpdateNode.path("param").path("quote").textValue());
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}

	/**
	 * Tests the api to get member external links data by passing user handle as parameter
	 */
	@Test(priority = 23, testName = "Get member external links data", 
			description = "Test get member external links data")
	public void testGetMemberExternalLinksData() {
		logger.info("MemberTest:testGetMemberExternalLinksData:Testing 'Get member external links data' api endpoint.");
		JsonNode userNode = rootNode.get("memberGetExternalLinksData");
		Iterator<JsonNode> elements = userNode.elements();
		JsonNode jsonUserObject = null;
		iterationCount=0;
		boolean flag = false;
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
		    String handle = jsonUserObject.path("param").path("handle").textValue();
			List<JsonNode> externalLinksData = util.getMemberExternalLinksData(handle);
			Assert.assertTrue(externalLinksData.size()>0);
			for (JsonNode jsonNode : externalLinksData) {
				if(jsonNode.has("URL") && jsonNode.has("key")){
					flag = true;
				}
				else
					flag = false;
			}
			Assert.assertTrue(flag);
			iterationCount++;
		}	
		Assert.assertEquals(iterationCount, userNode.size());
	}
	
	/**
	 * Tests the apis to update member external link and delete member external links by passing user handle as parameter 
	 */
	@Test(priority = 24, testName = "Update member external link", 
			description = "Test update member external link")
	public void testUpdateMemberExternalLink() {
		logger.info("MemberTest:testUpdateMemberExternalLink:Testing 'Update member external link' api endpoint.");
		JsonNode updateExternalLink = rootNode.get("updateExternalLink");
		JsonNode userNode = rootNode.get("memberToUpdateExternalLink");
		Iterator<JsonNode> elements = updateExternalLink.elements();
		JsonNode jsonUserObject = null;
		UserInfo userInfo=null;
		iterationCount=0;
		String handle = null;
		List<NameValuePair> headers = null;
		boolean flag = false;
		List<String> externalLinksIds = new ArrayList<String>();
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			headers = util.getHeader(handle,userInfo.getPassword());
			break;
		}
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			String url = jsonUserObject.path("param").path("url").asText().replace("@handle@",handle);
			((ObjectNode)jsonUserObject.path("param")).remove("url");
			((ObjectNode)jsonUserObject.path("param")).put("url", url);
			JsonNode externalLinkAfterUpdate = util.updateMemberExternalLinksData(handle, headers, jsonUserObject.toString());
			Assert.assertTrue(externalLinkAfterUpdate.path("result").path("content").path("URL").asText().equalsIgnoreCase(jsonUserObject.path("param").path("url").textValue()));
			externalLinksIds.add(externalLinkAfterUpdate.path("result").path("content").path("key").asText());
			iterationCount++;
		}	
		List<JsonNode> externalLinksData = util.getMemberExternalLinksData(handle);
		Assert.assertTrue(externalLinksData.size()>0);
		for (JsonNode jsonNode : externalLinksData) {
			if(jsonNode.path("URL").textValue().equalsIgnoreCase(jsonUserObject.path("param").path("url").textValue())){
				flag = true;
			}
			else
				continue;
		}
		Assert.assertTrue(flag);
		
		//Deleting one of the added external link
		for (String externalLinkId : externalLinksIds) {
			JsonNode externalLinkAfterDelete = util.deleteMemberExternalLinksData(handle, headers, externalLinkId);
			 Assert.assertTrue(externalLinkAfterDelete.path("result").path("content").path("key").textValue().equalsIgnoreCase(externalLinkId));
			 break;
		}
		
		//Verify that the deleted external link is no more available in the get call
		externalLinksData = util.getMemberExternalLinksData(handle);
		Assert.assertTrue(externalLinksData.size()>0);
		for (JsonNode jsonNode : externalLinksData) {
			for (String externalLinkId : externalLinksIds) {
			if(jsonNode.path("key").textValue().equalsIgnoreCase(externalLinkId)){
				continue;
			}
			else {
				flag = true;
			}
			}
			
		}
		Assert.assertTrue(flag);
		Assert.assertEquals(iterationCount, updateExternalLink.size());
	}
	
	/**
	 * Tests the api to delete member external link by passing user handle as parameter
	 */
	@Test(priority = 25, testName = "Delete member external link", 
			description = "Test delete member external link")
	public void testDeleteMemberExternalLink() {
		logger.info("MemberTest:testUpdateMemberExternalLink:Testing 'Delete member external link' api endpoint.");
		JsonNode updateExternalLink = rootNode.get("updateExternalLink");
		JsonNode userNode = rootNode.get("memberToUpdateExternalLink");
		Iterator<JsonNode> elements = updateExternalLink.elements();
		JsonNode jsonUserObject = null;
		UserInfo userInfo=null;
		iterationCount=0;
		String handle = null;
		List<NameValuePair> headers = null;
		List<String> externalLinksIds = new ArrayList<String>();
		boolean flag = false;
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			headers = util.getHeader(handle,userInfo.getPassword());
			break;
		}
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();		
			String url = jsonUserObject.path("param").path("url").asText().replace("@handle@",handle);
			((ObjectNode)jsonUserObject.path("param")).remove("url");
			((ObjectNode)jsonUserObject.path("param")).put("url", url);
		    JsonNode externalLinkAfterUpdate = util.updateMemberExternalLinksData(handle, headers, jsonUserObject.toString());
		    Assert.assertTrue(externalLinkAfterUpdate.path("result").path("content").path("URL").asText().equalsIgnoreCase(jsonUserObject.path("param").path("url").textValue()));
		    externalLinksIds.add(externalLinkAfterUpdate.path("result").path("content").path("key").asText());
			iterationCount++;
			break;
		}	
		for (String externalLinkId : externalLinksIds) {
			JsonNode externalLinkAfterDelete = util.deleteMemberExternalLinksData(handle, headers, externalLinkId);
			 Assert.assertTrue(externalLinkAfterDelete.path("result").path("content").path("key").textValue().equalsIgnoreCase(externalLinkId));
			 break;
		}
		
		List<JsonNode> externalLinksData = util.getMemberExternalLinksData(handle);
		Assert.assertTrue(externalLinksData.size()>0);
		for (JsonNode jsonNode : externalLinksData) {
			for (String externalLinkId : externalLinksIds) {
			if(jsonNode.path("key").textValue().equalsIgnoreCase(externalLinkId)){
				continue;
			}
			else {
				flag = true;
			}
			}
			
		}
		Assert.assertTrue(flag);
		//Assert.assertEquals(iterationCount, updateExternalLink.size());
	}

	/**
	 * Tests the api to update member external link by passing user handle as parameter
	 */
	@Test(priority = 26, testName = "Update other member external link", 
			description = "Test update other member external link", expectedExceptions= DefaultRequestProcessorException.class, enabled=false)
	public void testUpdateOtherMemberExternalLink() {
		logger.info("MemberTest:testUpdateOtherMemberExternalLink:Testing 'Update member external link' api endpoint for some other user.");
		JsonNode updateExternalLink = rootNode.get("updateExternalLink");
		Iterator<JsonNode> elements = updateExternalLink.elements();
		JsonNode jsonUserObject = null;
		UserInfo userInfo=null;
		iterationCount=0;
		String handle = null;
		List<NameValuePair> headers = null;
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			//Login with some other user
			headers = util.getHeader();
			break;
		}
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();
			String url = jsonUserObject.path("param").path("url").asText().replace("@handle@",handle);
			((ObjectNode)jsonUserObject.path("param")).remove("url");
			((ObjectNode)jsonUserObject.path("param")).put("url", url);
			JsonNode externalLinkAfterUpdate = util.updateMemberExternalLinksData(handle, headers, jsonUserObject.toString());
			iterationCount++;
		}	
	}
	
	@Test(priority = 27, testName = "Delete other member external link", 
			description = "Test other delete member external link", expectedExceptions= DefaultRequestProcessorException.class)
	public void testDeleteOtherMemberExternalLink() {
		logger.info("MemberTest:testUpdateMemberExternalLink:Testing 'Delete other member external link' api endpoint.");
		JsonNode updateExternalLink = rootNode.get("updateExternalLink");
		Iterator<JsonNode> elements = updateExternalLink.elements();
		JsonNode jsonUserObject = null;
		UserInfo userInfo=null;
		iterationCount=0;
		String handle = null;
		List<NameValuePair> headers = null;
		List<String> externalLinksIds = new ArrayList<String>();
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			headers = util.getHeader(handle,userInfo.getPassword());
			break;
		}
		//Iterate over set of member parameters one by one
		while(elements.hasNext()) {
			jsonUserObject = elements.next();		
			String url = jsonUserObject.path("param").path("url").asText().replace("@handle@",handle);
			((ObjectNode)jsonUserObject.path("param")).remove("url");
			((ObjectNode)jsonUserObject.path("param")).put("url", url);
		    JsonNode externalLinkAfterUpdate = util.updateMemberExternalLinksData(handle, headers, jsonUserObject.toString());
		    Assert.assertTrue(externalLinkAfterUpdate.path("result").path("content").path("URL").asText().equalsIgnoreCase(jsonUserObject.path("param").path("url").textValue()));
		    externalLinksIds.add(externalLinkAfterUpdate.path("result").path("content").path("key").asText());
			iterationCount++;
		}	
		for (String externalLinkId : externalLinksIds) {
			headers = util.getHeader();
			JsonNode externalLinkAfterDelete = util.deleteMemberExternalLinksData(handle, headers, externalLinkId);
			break;
		}
		
	}
	
	/**
	 * Tests the api to update member external link by passing user handle as parameter
	 */
	@Test(priority = 28, testName = "Adding same member external links twice", 
			description = "Test adding same member external links twice", expectedExceptions= DefaultRequestProcessorException.class)
	public void testAddSameMemberExternalLink() {
		logger.info("MemberTest:testAddSameMemberExternalLink:Testing 'Update member external link' api endpoint by adding same member external links twice");
		JsonNode updateExternalLink = rootNode.get("updateExternalLink");
		Iterator<JsonNode> elements = updateExternalLink.elements();
		JsonNode jsonUserObject = null;
		UserInfo userInfo=null;
		iterationCount=0;
		String handle = null;
		List<NameValuePair> headers = null;
		for(String newUser:newUsersMap.keySet()) {
			userInfo = newUsersMap.get(newUser);
			handle = userInfo.getUserName();
			headers = util.getHeader(handle,userInfo.getPassword());
			break;
		}
		//Iterate over set of member parameters one by one
		//Adding same external links twice
		int count = 0;
		while(count<2) {
			elements = updateExternalLink.elements();
			while(elements.hasNext()) {
				jsonUserObject = elements.next();
				String url = jsonUserObject.path("param").path("url").asText().replace("@handle@",handle);
				((ObjectNode)jsonUserObject.path("param")).remove("url");
				((ObjectNode)jsonUserObject.path("param")).put("url", url);
				JsonNode externalLinkAfterUpdate = util.updateMemberExternalLinksData(handle, headers, jsonUserObject.toString());
				iterationCount++;
			}
			count++;
		}
	}
}