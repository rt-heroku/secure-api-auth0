package com.heroku.security;

import org.springframework.stereotype.Component;

import com.auth0.Auth0;
import com.auth0.authentication.AuthenticationAPIClient;
import com.auth0.authentication.DatabaseConnectionRequest;
import com.auth0.authentication.result.Credentials;
import com.auth0.authentication.result.DatabaseUser;
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
    private Credentials credentials;
    
    public Auth0Client(String clientid, String domain) {
        this.setClientid(clientid);
        this.setDomain(domain);
        
        this.auth0 = new Auth0(clientid, domain);
        this.client = this.auth0.newAuthenticationAPIClient();
    }

    public Credentials login(String username, String password){
    	this.credentials = client.login(username, password).setScope("openid roles").execute();
    	return this.credentials;
    }

    public DatabaseUser createUser(String email, String password, String username){
    	DatabaseConnectionRequest<DatabaseUser> dcr = client.createUser(email, password).addParameter("app-metadata", "{\"roles\": [\"user\",\"ROLE_USER\",\"ROLE_ADMIN\"]}");
    	DatabaseUser du = dcr.execute();
    	return du;
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

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}