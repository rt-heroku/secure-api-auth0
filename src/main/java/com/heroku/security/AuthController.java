package com.heroku.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.authentication.result.Credentials;

@RestController
@RequestMapping()
public class AuthController {

	@Autowired
	private Auth0Client auth0Client;

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> doLogin(@RequestParam String username, @RequestParam String password) {
		Credentials credentials = auth0Client.login(username, password);
		Map<String, String> cs = new HashMap<String, String>();

		cs.put("id_token", credentials.getIdToken());
		cs.put("token_type", credentials.getType());
		cs.put("access_token", credentials.getAccessToken());
		cs.put("refresh_token", credentials.getRefreshToken());

		return cs;
	}
}
