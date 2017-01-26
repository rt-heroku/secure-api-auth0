package com.heroku.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.auth0.spring.security.api.Auth0SecurityConfig;



@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AppConfig extends Auth0SecurityConfig {

    /**
     * Provides Auth0 API access
     */
    @Bean
    public Auth0Client auth0Client() {
        return new Auth0Client(clientId, issuer);
    }

    /**
     *  Our API Configuration - for Profile CRUD operations
     *
     *  Here we choose not to bother using the `auth0.securedRoute` property configuration
     *  and instead ensure any unlisted endpoint in our config is secured by default
     */
    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
//                .antMatchers(HttpMethod.GET, "/contacts").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/v1/profiles/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/v1/profiles/**").hasAnyAuthority("ROLE_ADMIN")
//                .antMatchers(HttpMethod.PUT, "/api/v1/profiles/**").hasAnyAuthority("ROLE_ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/api/v1/profiles/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
    }

    /*
     * Only required for sample purposes..
     */
    String getAuthorityStrategy() {
       return super.authorityStrategy;
    }



}