package com.revature.JWTControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.revature.JWTServices.JWTService;

@RestController
@RequestMapping("")
public class JWTController {
	
	@Value("localhost:8080/auth-service/")
	String uri;
	
	@Autowired
	JWTService jServ;
	
	@GetMapping("jwtgenerate")
	public String generateToken(String email, String name)
	{
		String token;
		try {
			token = jServ.generateToken(email, name);
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return token;
	}
	
	@GetMapping("jwtcheck")
	public Boolean checkToken (String token)
	{
		String checkEmail = jServ.ExtractandDecodeJWT(token);
		if (checkEmail != null)
		{
			return true;
		}
		else
			return false;
	}
}
