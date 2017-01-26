package com.heroku.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Roles {
    public final String ADMIN;
    public final String SECURE;
    @Autowired
    public Roles(Environment env) {
        ADMIN = env.getProperty("stormpath.authorized.group.admin");
        SECURE = env.getProperty("stormpath.authroized.group.secure");
    }

}
