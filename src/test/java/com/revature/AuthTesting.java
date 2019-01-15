package com.revature;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.expression.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.revature.Service.AuthServiceTest;

public class AuthTesting {
	public AuthServiceTest aTest = new AuthServiceTest();
	@Test
	@Ignore
	public void TestingJWT() throws KeyLengthException, ParseException, JOSEException, BadJOSEException, java.text.ParseException, MalformedURLException {
		assertTrue(aTest.authTest());
	}
	
	@Test
	public void TestingNewToken() throws JOSEException
	{
		assertTrue(aTest.TokenTest());
	}
	
	@Test
	public void TestingFakeToken() throws MalformedURLException
	{
		assertTrue(aTest.FakeTokenTest());
	}
	
	@Test
	public void CognitoTest() throws MalformedURLException
	{
		assertTrue(aTest.CognitoTest());
	}
}
