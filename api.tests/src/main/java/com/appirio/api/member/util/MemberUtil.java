package com.appirio.api.member.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.appirio.api.member.config.MemberConfiguration;
import com.appirio.api.member.model.User;
import com.appirio.automation.api.DefaultRequestProcessor;
import com.appirio.automation.api.DefaultResponse;
import com.appirio.automation.api.config.EnvironmentConfiguration;
import com.appirio.automation.api.exception.AutomationException;
import com.appirio.automation.api.exception.InvalidRequestException;
import com.appirio.automation.api.model.UserInfo;
import com.appirio.automation.api.service.AuthenticationService;
import com.appirio.automation.api.service.UserService;
import com.appirio.automation.api.util.ApiUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MemberUtil {
	
	AuthenticationService  authService = new AuthenticationService();
	String jwtToken ="";
	final static Logger logger = Logger.getLogger(MemberUtil.class);
	UserService userService = new UserService();
	/**
	 * Gets and loads json data file
	 * @param fileName
	 * @return jsonContents
	 */
	public String getFile(String fileName) {
		  String jsonContents = "";
	 	  ClassLoader classLoader = getClass().getClassLoader();
		  try {
			  jsonContents = IOUtils.toString(classLoader.getResourceAsStream(fileName));
		  } catch (IOException e) {
			 throw new AutomationException("Some error occurred while reading member.json " 
					 	+ e.getLocalizedMessage());
		  }
		  return jsonContents;
	 }
	
	/**
	 * Generate jwt token(v3)
	 * @return headers
	 */
	public List<NameValuePair> getHeader()
	{
		authService = new AuthenticationService();
		//Get v3 token
		jwtToken=authService.getV3JWTToken();
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Bearer "+ jwtToken));
		return headers;
	}
	
	/**
	 * Generate jwt token(v3) of specific user
	 * @param username
	 * @param password
	 * @return headers
	 */
	public List<NameValuePair> getHeader(String username,String password)
	{
		authService = new AuthenticationService();
		//Get v3 token
		jwtToken=authService.getV3JWTToken(username,password);
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Bearer "+ jwtToken));
		return headers;
	}
	
	/**
	 * Generate jwt token(v2) of specific user
	 * @param username
	 * @param password
	 * @return headers
	 */
	public List<NameValuePair> getHeaderV2(String username,String password)
	{
		authService = new AuthenticationService();
		//Get v3 token
		jwtToken=authService.getAuth0JWTToken(username,password);
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Bearer "+ jwtToken));
		return headers;
	}
	
	/**
	 * Generate jwt token(v2)
	 * @return headers
	 */
	public List<NameValuePair> getHeaderV2()
	{
		authService = new AuthenticationService();
		//Get v3 token
		jwtToken=authService.getAuth0JWTToken();
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		headers.add(new BasicNameValuePair("Authorization", "Bearer "+ jwtToken));
		return headers;
	}
	
	/**
	 * Get token from photo url in the response content.
	 * @param jsonObject
	 * @return token
	 */
	public String getTokenFromPhotoUrl(JsonNode jsonObject){
		String token ="";
		String jsonContentValue = jsonObject.path("result").path("content").textValue();
		token = jsonContentValue.substring(jsonContentValue.indexOf("profile/"), jsonContentValue.indexOf(".jp"));
		token=token.substring(token.lastIndexOf("-")+1);
		return token;
		
	}
	
	
	
	/**
	 * Gets the profile of a specific member
	 * @param handle
	 * @param headers
	 * @return responseMetadata
	 */
	public DefaultResponse retrieveMemberProfile(String handle,List<NameValuePair> headers){
		String retrieveMemberProfile = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getRetrieveMemberProfileEndPoint();
		retrieveMemberProfile = ApiUtil.replaceToken(retrieveMemberProfile,"@handle",handle);
		DefaultResponse responseMetadata=DefaultRequestProcessor.getRequest(retrieveMemberProfile, null, headers);
		return responseMetadata;
	}
	
	/**
	 * Updates the profile of a member using UserInfo object contain member data
	 * @param userInfo
	 * @param memberUpdateNode
	 * @return
	 */
	public DefaultResponse updateMemberProfile(UserInfo userInfo,JsonNode memberUpdateNode){
		JsonNode memberNode = memberUpdateNode.get("param");
		((ObjectNode)memberNode).put("firstName",userInfo.getfirstName());
		((ObjectNode)memberNode).put("lastName",userInfo.getLastName());
		((ObjectNode)memberNode).put("handle",userInfo.getUserName());
		((ObjectNode)memberNode).put("email",userInfo.getEmail());
		List<NameValuePair> headers = getHeader(userInfo.getUserName(),userInfo.getPassword());
		String putMemberProfile = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberPutProfileEndPoint();
		putMemberProfile= ApiUtil.replaceToken(putMemberProfile,"@handle",userInfo.getUserName());
		DefaultResponse responseMetadata = DefaultRequestProcessor.putRequest(putMemberProfile, null, headers,memberUpdateNode.toString());
		return responseMetadata;
	
	}
	
	/**
	 * Updates the profile of a member with specified data 
	 * @param userDetails
	 * @param memberUpdateNode
	 * @return memberUpdateNode
	 * 			json object containing all the updated member details
	 */
	public JsonNode addUpdatedFieldsToMemberObj(JsonNode userDetails,JsonNode memberUpdateNode){
			JsonNode memberNode = memberUpdateNode.path("param");
			String userId = userDetails.path("param").path("userId").textValue();
			String photoURL = userDetails.path("param").path("photoURL").textValue();
			String firstName = userDetails.path("param").path("firstName").textValue();
			String lastName = userDetails.path("param").path("lastName").textValue();
			String handle = userDetails.path("param").path("handle").textValue();
			String email = userDetails.path("param").path("email").textValue();
			String otherLangName = userDetails.path("param").path("otherLangName").textValue();
			String quote = userDetails.path("param").path("quote").textValue();
			((ObjectNode)memberNode).put("userId",userId);
			((ObjectNode)memberNode).put("firstName",firstName);
			((ObjectNode)memberNode).put("lastName",lastName);
			((ObjectNode)memberNode).put("handle",handle);
			((ObjectNode)memberNode).put("email",email);
			((ObjectNode)memberNode).put("otherLangName",otherLangName);
			((ObjectNode)memberNode).put("quote",quote);
			((ObjectNode)memberNode).put("photoURL",photoURL);
			return memberUpdateNode;
		}
		
		/**
		 * Asserts the fields in member skill set got in the response contents
		 * @param skills
		 * @param handle
		 * @return result
		 */
		public boolean assertFieldsInMemberSkillSet(JsonNode skills,String handle){
			Iterator<JsonNode> skillset = skills.elements();
			boolean result = false;
			while(skillset.hasNext()) {
				JsonNode temp = skillset.next();
				Iterator<String> skillTags =temp.fieldNames();
				JsonNode field = null;
				while(skillTags.hasNext()) {
					String nodeFieldName = skillTags.next();
					if(nodeFieldName.equalsIgnoreCase("tagName")){
						field = temp.get(nodeFieldName);
						if(!field.equals(NullNode.getInstance())){
							result = true;
						}
					else{
							logger.error("MemberUtil:assertFieldsInMemberSkillSet:Skills for member '"+handle+"' does not contain field 'tagName'");
							return false;
						}
					}
					else if(nodeFieldName.equalsIgnoreCase("sources")){
						field = temp.get(nodeFieldName);
						if(!field.equals(NullNode.getInstance())){
							result = true;
						}
						else{
							logger.error("MemberUtil:assertFieldsInMemberSkillSet:Skills for member '"+handle+"' does not contain field 'sources'");
							return false;
						}
					}
					else if(nodeFieldName.equalsIgnoreCase("score")){
						field = temp.get(nodeFieldName);
						if(!field.equals(NullNode.getInstance())){
							result = true;
						}
						else{
							logger.error("MemberUtil:assertFieldsInMemberSkillSet:Skills for member '"+handle+"' does not contain field 'score'");
							return false;
						}
					}
					else if(nodeFieldName.equalsIgnoreCase("hidden")){
						field = temp.get(nodeFieldName);
						if(!field.equals(NullNode.getInstance())){
							result = true;
						}
						else{
							logger.error("MemberUtil:assertFieldsInMemberSkillSet:Skills for member '"+handle+"' does not contain field 'hidden'");
							return false;
						}
					}
				}
			}
			return result;
		}
		
	/**
	 * Adds a node containing tagId and its value for 'hidden' field, to the skillset node
	 * @param skillsSet
	 * @param tagId
	 * @param hidden
	 * @return skillsSet
	 */
	public JsonNode addSkillTagIds(ObjectNode skillsSet,String tagId,String hidden) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode hidden2 = mapper.createObjectNode();;
		hidden2.put("hidden", hidden);
		skillsSet.put(tagId,hidden2);
		return skillsSet;
		
	}
	
	/**
	 * Constructs url parameters for member api endpoints where filter values are field names
	 * @param requestObject
	 * 			json object cointaining field names to used as filter parameters
	 * @return urlParameters
	 * 			List<NameValuePair> type object containing all parameters
	 */
	public List<NameValuePair> constructParameters(JsonNode requestObject) {
		Iterator<String> fieldNames = requestObject.fieldNames();
		List<NameValuePair> urlParameters=new ArrayList<NameValuePair>();
		while(fieldNames.hasNext()) {
			String filterValues="";
			String fieldName = fieldNames.next();
		if(fieldName.equalsIgnoreCase("fields"))
			{
				JsonNode jsonFilter=requestObject.get(fieldName);
				Iterator<String> filterfieldNames = jsonFilter.fieldNames();
				while(filterfieldNames.hasNext()) {
					String filterFieldName=filterfieldNames.next();
					filterValues=filterValues+filterFieldName+",";
				}
				filterValues=filterValues.substring(0, filterValues.lastIndexOf(","));
				urlParameters.add(new BasicNameValuePair("fields",filterValues));
			}
		}
		return urlParameters;
	}
	
	/**
	 * Checks if the response contents are as per the filter values and contain the specified field
	 * @param response
	 * 			response obtained by executing the query
	 * @param filterFieldName
	 * 			field name to be checked in the response contents
	 * @return
	 */
	public boolean assertFieldsAsFilterParams(DefaultResponse response,String filterFieldName){
		JsonNode responseData = null;
		List<JsonNode> responseContents = new ArrayList<JsonNode>();
		String nodeFieldName;
		if(response.getResponseContents()==null){
			responseData=response.getResponseData();
			responseContents.add(responseData.path("result").path("content"));
		}
		else
			responseContents = response.getResponseContents();
		for (JsonNode jsonNode : responseContents) {
		Iterator<String> elements =jsonNode.fieldNames();
		while(elements.hasNext()) {
			nodeFieldName = elements.next();
			if(nodeFieldName.equalsIgnoreCase(filterFieldName))
					return true;
				else
					continue;
		
		}
		}
		
		return false;
	}
	public Map<String,User> getUsers(JsonNode users) {
		Map<String,User> usersMap = new HashMap<String,User>();
		JsonNode existingUserNode = null;
		Iterator<JsonNode> elements = users.elements();
		while(elements.hasNext()) {
			existingUserNode = elements.next();
			JsonNode paramNode = existingUserNode.path("param");
			User u = new User(paramNode.path("userId").textValue(),paramNode.path("username").textValue(), 
					paramNode.path("password").textValue());
			usersMap.put(u.getUsername(),u);
		}
		return usersMap;
	}
	
	/**
	 * Creates test users
	 * @param usersNode
	 * @return newUsersMap
	 */
	public Map<String,UserInfo> createUsers(JsonNode usersNode) {
		Iterator<JsonNode> iter = usersNode.elements();
		JsonNode usersObject    = null;
		ArrayList<UserInfo> usersList = new ArrayList<UserInfo>();
		Map<String,UserInfo> newUsersMap = new HashMap<String,UserInfo>();
		while(iter.hasNext()) {
			usersObject = iter.next();
			UserInfo userInfo = userService.createAnActivatedUser(usersObject);
			logger.debug("MemberTest:setUp:createUsers:User with Id " + userInfo.getUserId() + " created");
			usersList.add(userInfo);
			newUsersMap.put(userInfo.getUserName(), userInfo);
		}
		logger.debug("MemberUtil:createUsers: " + usersList.size() + " Users created!");
		return newUsersMap;
	}


	/**
	 * Gets External Links Data of a specific member
	 * @param handle
	 * @return responseMetadata
	 */
	public List<JsonNode> getMemberExternalLinksData(String handle){
		String getExternalLinksDataUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberGetExternalLinksData();
		getExternalLinksDataUrl = ApiUtil.replaceToken(getExternalLinksDataUrl,"@handle@",handle);
		DefaultResponse responseMetadata=DefaultRequestProcessor.getRequest(getExternalLinksDataUrl, null, null);
		return responseMetadata.getResponseContents();
	}
	
	/**
	 * Update External Link of a specific member
	 * @param handle
	 * @return responseMetadata
	 */
	public JsonNode updateMemberExternalLinksData(String handle,List<NameValuePair> headers, String payload){
		String updateMemberExternalLinkUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberUpdateExternalLink();
		updateMemberExternalLinkUrl = ApiUtil.replaceToken(updateMemberExternalLinkUrl,"@handle@",handle);
		DefaultResponse responseMetadata= DefaultRequestProcessor.postRequest(updateMemberExternalLinkUrl,null,headers,payload);
		return responseMetadata.getResponseData();
	}
	
	/**
	 * Delete External Link of a specific member
	 * @param handle
	 * @return responseMetadata
	 */
	public JsonNode deleteMemberExternalLinksData(String handle,List<NameValuePair> headers, String externalLinkId){
		String deleteMemberExternalLinkUrl = EnvironmentConfiguration.getBaseUrl()+MemberConfiguration.getMemberDeleteExternalLink();
		deleteMemberExternalLinkUrl = ApiUtil.replaceToken(deleteMemberExternalLinkUrl,"@handle@",handle);
		deleteMemberExternalLinkUrl = ApiUtil.replaceToken(deleteMemberExternalLinkUrl,"@externalLinkId@",externalLinkId);
		DefaultResponse responseMetadata= DefaultRequestProcessor.deleteRequest(deleteMemberExternalLinkUrl, null, headers);
		return responseMetadata.getResponseData();
	}
}
