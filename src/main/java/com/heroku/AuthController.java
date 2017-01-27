package com.heroku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.authentication.result.Credentials;
import com.heroku.model.ResponseMessage;
import com.heroku.model.SimpleRecord;
import com.heroku.security.Auth0Client;

@RestController
@RequestMapping()
public class AuthController {

	@Autowired
	private Auth0Client auth0Client;

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessage doLogin(@RequestParam String username, @RequestParam String password) {
		Credentials credentials = auth0Client.login(username, password);
		List<SimpleRecord> cs = new ArrayList<SimpleRecord>();

		cs.add(new SimpleRecord("id_token", credentials.getIdToken()));
		cs.add(new SimpleRecord("token_type", credentials.getType()));
		cs.add(new SimpleRecord("access_token", credentials.getAccessToken()));
		cs.add(new SimpleRecord("refresh_token", credentials.getRefreshToken()));

		ResponseMessage rm = new ResponseMessage("", true, cs);
		return rm;
	}
}
