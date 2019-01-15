package com.revature.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.cognitoidp.*;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AmazonCognitoConfig {

	private String clientId;
	private String userPoolId;
	private String endPoint;
	private String region;
	private String identityPoolId;
	
	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
	      ClasspathPropertiesFileCredentialsProvider propertiesFileCredentialsProvider = 
	           new ClasspathPropertiesFileCredentialsProvider();
	 
	       return AWSCognitoIdentityProviderClientBuilder.standard()
	                      .withCredentials(propertiesFileCredentialsProvider)
	                             .withRegion(getRegion())
	                             .build();
	 
	}
	
	public AmazonCognitoConfig(String clientId, String userPoolId, String endPoint, String region,
			String identityPoolId) {
		super();
		this.clientId = clientId;
		this.userPoolId = userPoolId;
		this.endPoint = endPoint;
		this.region = region;
		this.identityPoolId = identityPoolId;
	}
	public AmazonCognitoConfig() {
		super();
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getUserPoolId() {
		return userPoolId;
	}
	public void setUserPoolId(String userPoolId) {
		this.userPoolId = userPoolId;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getIdentityPoolId() {
		return identityPoolId;
	}
	public void setIdentityPoolId(String identityPoolId) {
		this.identityPoolId = identityPoolId;
	}
	
}
