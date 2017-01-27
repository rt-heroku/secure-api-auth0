package com.heroku;

import com.auth0.APIException;
import com.auth0.authentication.result.Credentials;
import com.auth0.spring.security.api.Auth0JWTToken;
import com.heroku.security.Auth0Client;

public class Auth0AuthenticationTests {

	String clientid = "KfDvsDJvE3vL24fMMmbyOYNrJG9JKCrH";
	String domain = "app62754487.auth0.com";
	String clientSecret = "7_hf3zCmA898NfHxldiGs_0hdBxCPpDujz1ZbHovigfJibL0e9a7eETx8-wfKEyK";

	public static void main(String[] args) {
		Auth0AuthenticationTests a = new Auth0AuthenticationTests();
		a.testLogin();
	}

	public void testLogin() {
		Auth0Client ac = new Auth0Client(clientid, domain);
		ac.setClientSecret(clientSecret);
		System.out.println("GOOD CREDENTIALS!!!!");
		try {
			Credentials good = ac.login("rt@heroku.com", "Password1");
			System.out.println("ID TOKEN:\n" + good.getIdToken());
			System.out.println("Type:" + good.getType());
			System.out.println("Access Token: " + good.getAccessToken());
			System.out.println("Refresh Token: " + good.getRefreshToken());
			Auth0JWTToken jwt = new Auth0JWTToken(good.getIdToken());
			System.out.println("Authenticated username: " + ac.getUsername(jwt));

		} catch (APIException e) {
			System.out.println(e.getMessage());
		}
	}
}
