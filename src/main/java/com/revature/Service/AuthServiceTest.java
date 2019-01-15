package com.revature.Service;

import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;

import org.junit.Test;
import org.springframework.expression.ParseException;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.revature.JWTServices.JWTService;

public class AuthServiceTest {
		/**
		 * JUnit testing for authorizing token takes in JWT String that is encrypted
		 * @return
		 * @throws JOSEException
		 * @throws java.text.ParseException
		 * @throws BadJOSEException
		 */
		public boolean authTest() throws JOSEException, java.text.ParseException, BadJOSEException
		{
			JWTClaimsSet claims = new JWTClaimsSet.Builder()
					  .claim("email", "sanjay@example.com")
					  .claim("name", "Sanjay Patel")
					  .build();

			Payload payload = new Payload(claims.toJSONObject());
			
			JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
			
			JWTService jServ = new JWTService();
			
			String token = jServ.Encrypter(header, payload);
			System.out.println(token);
			
			String finalString = jServ.ExtractandDecodeJWT(token);
			
			if(finalString.isEmpty())
			{
				return false;
			}
			else
			{
				System.out.println(finalString);
				return true;
			}
		}
		
		/**
		 * JUnit Test for JWT encrypting must submit two strings one name and email
		 * @return Encrypted JWT String
		 * @throws JOSEException
		 */
		public boolean TokenTest() throws JOSEException
		{
			String name = "user1";
			String email = "user1@revature.com";
			JWTService jServ = new JWTService();
			if (jServ.generateToken(email, name).isEmpty())
			{
				return false;
			}
			else
				return true;
		}
		
		public boolean FakeTokenTest()
		{
			String fakeToken = "SDFASFDASDFSADF";
			JWTService jServ = new JWTService();
			if (jServ.ExtractandDecodeJWT(fakeToken) == null)
			{
				return true;
			}
			return false;
		}
}
