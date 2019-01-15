package com.revature.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.revature.config.AmazonCognitoConfig;
import com.revature.models.AuthRequest;
import com.revature.models.UserSignUpRequest;

@Controller
@RequestMapping("")
public class AuthController {

	@Autowired
	AmazonCognitoConfig amazonCognitoConfig;
	
	@PostMapping("/signup")
	@ResponseBody
	public UserType signUp(@RequestBody UserSignUpRequest signUpRequest) {
		AWSCognitoIdentityProvider cognitoClient = amazonCognitoConfig.getAmazonCognitoIdentityClient();
		AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
				.withUserPoolId(amazonCognitoConfig.getUserPoolId())
				.withUsername(signUpRequest.getUsername())
				.withUserAttributes(
						new AttributeType()
						.withName("name")
			              .withValue(signUpRequest.getFirstname()),
			            new AttributeType()
			            .withName("email")
			            .withValue(signUpRequest.getUsername()),
			            new AttributeType()
			            .withName("email_verified")
			              .withValue("true"))
						.withTemporaryPassword("numberoflegs")
						.withMessageAction("SUPPRESS")
						.withForceAliasCreation(Boolean.FALSE);
		
		AdminCreateUserResult createUserResult = cognitoClient.adminCreateUser(cognitoRequest);
		UserType cognitoUser = createUserResult.getUser();
		return cognitoUser;
	}
	
	
	
	@PostMapping("/login")
	@ResponseBody
	public AuthenticationResultType signIn(@RequestBody AuthRequest authRequest) {
		AuthenticationResultType authenticationResult = null;
		AWSCognitoIdentityProvider cognitoClient = amazonCognitoConfig.getAmazonCognitoIdentityClient();
	
		final Map<String, String> authParameters = new HashMap<>();
		authParameters.put("USERNAME", authRequest.getUsername());
		authParameters.put("PASSWORD", authRequest.getPassword());
		
		final AdminInitiateAuthRequest request = new AdminInitiateAuthRequest();
	       request.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
	       .withClientId(amazonCognitoConfig.getClientId())
	       .withUserPoolId(amazonCognitoConfig.getUserPoolId())
	       .withAuthParameters(authParameters);
	 
	   AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(request);
	   
	   if(StringUtils.isNotBlank(result.getChallengeName())) {
		   if("NEW_PASSWORD_REQUIRED".equals(result.getChallengeName())) {
			   authParameters.put("NEW_PASSWORD", authRequest.getNewPassword());
			   
			   final AdminRespondToAuthChallengeRequest challengeRequest =
					   new AdminRespondToAuthChallengeRequest();
			   challengeRequest.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
			       .withChallengeResponses(authParameters)
			       .withClientId(amazonCognitoConfig.getClientId())
			       .withUserPoolId(amazonCognitoConfig.getUserPoolId())
			       .withSession(result.getSession());
			   
			   AdminRespondToAuthChallengeResult resultChallenge = 
					   cognitoClient.adminRespondToAuthChallenge(challengeRequest);
			   
			   authenticationResult = resultChallenge.getAuthenticationResult();

		   }
	   }
	   
	   cognitoClient.shutdown();
	   
	   return result.getAuthenticationResult();
	   
	}
	
}
