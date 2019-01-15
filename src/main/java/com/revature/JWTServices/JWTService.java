package com.revature.JWTServices;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.util.Base64;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

public class JWTService {
	public final String secret = "841D8A6C80CBA4FCAD32D5367C18C53B";
	public final byte[] secretKey = secret.getBytes();
	//4min timeout
	private static final int SESSIONTIMEOUT = 200000;
	
	/**
	 * @author anthonydevoz 1811 Java Nick (1/15/19)
	 * Takes encrypted JWT token as a string and decodes will return null if the token is invalid;
	 * @param token
	 * @return String of email if confirmed
	 */
	public String ExtractandDecodeJWT(String token)
	{
		ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<SimpleSecurityContext>();
		JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<SimpleSecurityContext>(secretKey);
		JWEKeySelector<SimpleSecurityContext> jweKeySelector =
		new JWEDecryptionKeySelector<SimpleSecurityContext>(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);
		jwtProcessor.setJWEKeySelector(jweKeySelector);
		

		JWTClaimsSet claims;
		try {
			claims = jwtProcessor.process(token, null);
		} catch (ParseException | BadJOSEException | JOSEException e) {
			e.printStackTrace();
			return null;
		}
		String email = (String) claims.getClaim("email");
		String name = (String) claims.getClaim("name");
		
		return email;
	}
	/**
	 * @author anthonydevoz 1811 Java Nick (1/15/19)
	 * Token encrypter used for generating JWT token. Takes in header and payload then returns encrypted JWT
	 * will return null if any failures during execution;
	 * @param header
	 * @param payload
	 * @return
	 */
	public String Encrypter(JWEHeader header, Payload payload)
	{
		byte[] secretKey = secret.getBytes();
		DirectEncrypter encrypter;
		try {
			encrypter = new DirectEncrypter(secretKey);
		} catch (KeyLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		JWEObject jweObject = new JWEObject(header, payload);
		try {
			jweObject.encrypt(encrypter);
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String token = jweObject.serialize();
		return token;
	}
	/**
	 * @author anthonydevoz 1811 Java Nick (1/15/19)
	 * Takes in login string and name string will place both inside claimset then encrypt using the encrypter 
	 * function uses A128CBC_HS256 encryption method
	 * @param login
	 * @param name
	 * @return
	 * @throws JOSEException
	 */
	public String generateToken(String login, String name) throws JOSEException
	{
		JWTClaimsSet claims = new JWTClaimsSet.Builder()
				  .claim("email", login)
				  .claim("name", name)
				  .expirationTime(new Date(System.currentTimeMillis() + SESSIONTIMEOUT))
				  .build();
		// needs incoginito payload
		System.out.println(claims.getExpirationTime());
		Payload payload = new Payload(claims.toJSONObject());
		
		JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
		
		JWTService jServ = new JWTService();
		String encryptedToken = jServ.Encrypter(header, payload);
		return encryptedToken;
	}
}
