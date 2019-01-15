package com.revature;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.expression.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.revature.Service.AuthServiceTest;

public class AuthTesting {
	public AuthServiceTest aTest = new AuthServiceTest();
	@Test
	public void TestingJWT() throws KeyLengthException, ParseException, JOSEException, BadJOSEException, java.text.ParseException {
		assertTrue(aTest.authTest());
	}
	
	@Test
	public void TestingNewToken() throws JOSEException
	{
		assertTrue(aTest.TokenTest());
	}
	
	@Test
	public void TestingFakeToken()
	{
		assertTrue(aTest.FakeTokenTest());
	}
}
