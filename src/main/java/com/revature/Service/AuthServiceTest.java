package com.revature.Service;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
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
		 * @throws MalformedURLException 
		 */
		public boolean authTest() throws JOSEException, java.text.ParseException, BadJOSEException, MalformedURLException
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
		
		public boolean FakeTokenTest() throws MalformedURLException
		{
			String fakeToken = "SDFASFDASDFSADF";
			JWTService jServ = new JWTService();
			if (jServ.ExtractandDecodeJWT(fakeToken) == null)
			{
				return true;
			}
			return false;
		}
		
		public boolean CognitoTest() throws MalformedURLException
		{
			String cognitoToken = "eyJraWQiOiJ4THBFdFJEXC9JSjg5WWVsQVNzVDdRZmZEYlQ3SGhBODRhK1Q0M3F2U1hDaz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxOTc1MDQ4ZS05ZGMwLTRhYmUtOGM1Ny1mYWI5NTZhN2M3ZTEiLCJhdWQiOiIxMzI4ZXE5YXJsZ3JmYm9hNGE0aG9xODk0diIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJldmVudF9pZCI6IjhiZjY2MWE3LTE4ZjMtMTFlOS1iM2QyLWFkMTc2MDZmODlhNSIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNTQ3NTc2OTkxLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0yLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMl9WWHViM0JSM24iLCJuYW1lIjoiQXVzdGluIiwiY29nbml0bzp1c2VybmFtZSI6IjE5NzUwNDhlLTlkYzAtNGFiZS04YzU3LWZhYjk1NmE3YzdlMSIsImV4cCI6MTU0NzU4MDU5MSwiaWF0IjoxNTQ3NTc2OTkxLCJlbWFpbCI6ImV4YW1wbGVlbWFpbEBleGFtcGxlLmNvbSJ9.cb8qfSOGfFRX9MBCR9wZBgfEuZAwVuADlqBeWwKVHjD9LYm2hhY__acuf21_heMU3TAogBZlN3xs0pXr4D_aKs4mtlR1wZmSRClIVzvawGfQ_cO_n2rBg_C5vIQyDBacuaLQrSNyg_M8-CVnAzjGi8fy28mk78vTZ7RZwcLrDaXlLr_jXHsJxg5497djKtl5PWvGZQb9jfvU0tCqrobFZqRqk_5PFAL68D3frjZYQVZmdyDgNna1t5BAqI0F4spjJ2B2z6kv-wMGlRf5SUnbCvah-zlekjisI00DA9vxYiYEsk9JVwIqqIS9Tx7WVWYx4PtRp0ZwOmDqMyZjcU8fgw";
			JWTService jService = new JWTService();
//			System.out.println(jService.ExtractandDecodeJWT(cognitoToken));
			return (jService.ExtractandDecodeJWT(cognitoToken) != null);
		}
}
