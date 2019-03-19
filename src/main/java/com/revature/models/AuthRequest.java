package com.revature.models;



/**
 * Simple Pojo to handle the signin request with cognito
 * we use the newPassword field for the first signin where the user
 * needs to set a new password
 * 
 * @author 1811-Java-Nick 12/27/18 Hugo, Zaryn
 * 
 */
public class AuthRequest {

	private String username;

	private String password;
	
	private String newPassword;
	
	public AuthRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "AuthRequest [username=" + username + ", password=" + password + "]";
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
