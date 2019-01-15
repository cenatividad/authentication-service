package com.revature.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.cognitoidp.*;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AmazonCognitoConfig {

	@Value("clientId")
	private String clientId;
	
	@Value("userPoolId")
	private String userPoolId;
	
	@Value("endPoint")
	private String endPoint;
	
	@Value("region")
	private String region;
	
	@Value("identityPoolId")
	private String identityPoolId;
	
	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		AWSCredentials aws = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY_ID"),System.getenv("AWS_SECRET_ACCESS_KEY"));
        return AWSCognitoIdentityProviderClientBuilder.standard()
                       .withCredentials(new AWSStaticCredentialsProvider(aws))
                              .withRegion(getRegion()).build();
	 
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
