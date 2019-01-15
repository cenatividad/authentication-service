package com.revature.controller;

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

/**
 * Authorization controller for the Auth Service Mainly a test service for
 * Cognito Contains signup and signin methods
 * 
 * @author 1811-Java-Nick 12/27/18 Hugo, Zaryn, Austin
 * 
 */
@Controller
@RequestMapping("")
public class AuthController {

	@Autowired
	AmazonCognitoConfig amazonCognitoConfig;

	/**
	 * 
	 * @param signUpRequest
	 *            Pojo containing the necessary informations for signing up
	 * @return the user (formated by cognito) with cognito user info
	 * @author 1811-Java-Nick 12/27/18 Hugo, Zaryn, Austin
	 */
	@PostMapping("/signup")
	@ResponseBody
	public UserType signUp(@RequestBody UserSignUpRequest signUpRequest) {
		// Creating a cognito user with data provided in the signUpRequest object
		AWSCognitoIdentityProvider cognitoClient = amazonCognitoConfig.getAmazonCognitoIdentityClient();
		AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
				.withUserPoolId(amazonCognitoConfig.getUserPoolId()).withUsername(signUpRequest.getUsername())
				.withUserAttributes(new AttributeType().withName("name").withValue(signUpRequest.getFirstname()),
						new AttributeType().withName("email").withValue(signUpRequest.getUsername()),
						new AttributeType().withName("email_verified").withValue("true"))
				.withTemporaryPassword("numberoflegs") // Temporary default password (needed for the first sign in where
														// you can set another one)
				.withMessageAction("SUPPRESS").withForceAliasCreation(Boolean.FALSE);

		AdminCreateUserResult createUserResult = cognitoClient.adminCreateUser(cognitoRequest);

		// Getting back the created user to return it
		UserType cognitoUser = createUserResult.getUser();
		return cognitoUser;
	}

	/**
	 * 
	 * @param AuthRequest
	 *            Pojo containing username, password and newPassword
	 * @return tokens used for authentication (access, id and refresh tokens)
	 * @author 1811-Java-Nick 12/27/18 Hugo, Zaryn, Austin
	 */
	@PostMapping("/login")
	@ResponseBody
	public AuthenticationResultType signIn(@RequestBody AuthRequest authRequest) {
		AuthenticationResultType authenticationResult = null;
		AWSCognitoIdentityProvider cognitoClient = amazonCognitoConfig.getAmazonCognitoIdentityClient();

		// Setting up the authParameters to send to cognito
		final Map<String, String> authParameters = new HashMap<>();
		authParameters.put("USERNAME", authRequest.getUsername());
		authParameters.put("PASSWORD", authRequest.getPassword());

		// Creating the cognito request
		final AdminInitiateAuthRequest request = new AdminInitiateAuthRequest();
		request.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withClientId(amazonCognitoConfig.getClientId())
				.withUserPoolId(amazonCognitoConfig.getUserPoolId()).withAuthParameters(authParameters);

		// Initiating the authentification
		AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(request);

		// Starting to check if there is challenges required for the user to pass
		// In our example there is a new_password_required for the first sign in
		if (StringUtils.isNotBlank(result.getChallengeName())) {
			if ("NEW_PASSWORD_REQUIRED".equals(result.getChallengeName())) {
				
				// this is why we have the "newPassword" field in authRequest pojo, to specify
				// the new password. The default temporary password was set to "numberoflegs"
				authParameters.put("NEW_PASSWORD", authRequest.getNewPassword());

				final AdminRespondToAuthChallengeRequest challengeRequest = new AdminRespondToAuthChallengeRequest();
				challengeRequest.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
						.withChallengeResponses(authParameters).withClientId(amazonCognitoConfig.getClientId())
						.withUserPoolId(amazonCognitoConfig.getUserPoolId()).withSession(result.getSession());

				//Sending the response to the challenges with the updated password
				AdminRespondToAuthChallengeResult resultChallenge = cognitoClient
						.adminRespondToAuthChallenge(challengeRequest);

				authenticationResult = resultChallenge.getAuthenticationResult();

			}
		}

		cognitoClient.shutdown();

		return result.getAuthenticationResult();

	}

}
