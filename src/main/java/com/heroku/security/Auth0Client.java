package com.heroku.security;

import org.springframework.stereotype.Component;

import com.auth0.Auth0;
import com.auth0.authentication.AuthenticationAPIClient;
import com.auth0.authentication.result.Credentials;
import com.auth0.authentication.result.UserProfile;
import com.auth0.request.Request;
import com.auth0.spring.security.api.Auth0JWTToken;

@Component
public class Auth0Client {

    private String clientid;
    private String domain;
    private String clientSecret;
    private AuthenticationAPIClient client;
    private Auth0 auth0;

    public Auth0Client(String clientid, String domain) {
        this.setClientid(clientid);
        this.setDomain(domain);
        
        this.auth0 = new Auth0(clientid, domain);
        this.client = this.auth0.newAuthenticationAPIClient();
    }

    public Credentials login(String username, String password, String connection){
    	return client.login(username, password).setConnection(connection).execute();
    }
    
    public String getUsername(Auth0JWTToken token) {
        Request<UserProfile> request = client.tokenInfo(token.getJwt());
        UserProfile profile = request.execute();
        return profile.getEmail();
    }

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

}