package com.auth0.spring.security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.auth0.jwt.Algorithm;
import com.auth0.spring.security.api.Auth0AuthenticationEntryPoint;
import com.auth0.spring.security.api.Auth0AuthenticationFilter;
import com.auth0.spring.security.api.Auth0AuthenticationProvider;
import com.auth0.spring.security.api.Auth0AuthorityStrategy;
import com.auth0.spring.security.api.Auth0CORSFilter;
import com.auth0.spring.security.api.authority.AuthorityStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2147483640)
@ConditionalOnProperty(prefix = "auth0", name = { "defaultAuth0ApiSecurityEnabled" })
public class Auth0HerokuConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth0.domain}")
	protected String domain;

	@Value("${auth0.issuer:https://${auth0.domain}/}")
	protected String issuer;

	@Value("${auth0.client.id}")
	protected String clientId;

	@Value("${auth0.client.secret}")
	protected String clientSecret;

	@Value("${auth0.secured.route:}")
	protected String securedRoute;

	@Value("${auth0.authority.strategy:ROLES}")
	protected String authorityStrategy;

	@Value("${auth0.base64.encoded.secret:false}")
	protected boolean base64EncodedSecret;

	@Value("${auth0.signing.algorithm:HS256}")
	protected String signingAlgorithm;

	@Value("${auth0.public.key.path:}")
	protected String publicKeyPath;

	@Autowired
	@Bean(name = { "auth0AuthenticationManager" })
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public Auth0CORSFilter simpleCORSFilter() {
		return new Auth0CORSFilter();
	}

	@Bean(name = { "authorityStrategy" })
	public AuthorityStrategy authorityStrategy() {
		if (!(Auth0AuthorityStrategy.contains(this.authorityStrategy))) {
			throw new IllegalStateException("Configuration error, illegal authority strategy");
		}
		return Auth0AuthorityStrategy.valueOf(this.authorityStrategy).getStrategy();
	}

	@Bean(name = { "auth0AuthenticationProvider" })
	public Auth0AuthenticationProvider auth0AuthenticationProvider() {
		Auth0AuthenticationProvider authenticationProvider = new Auth0AuthenticationProvider();
		authenticationProvider.setDomain(this.domain);
		authenticationProvider.setIssuer(this.issuer);
		authenticationProvider.setClientId(this.clientId);
		authenticationProvider.setClientSecret(this.clientSecret);
		authenticationProvider.setSecuredRoute(this.securedRoute);
		authenticationProvider.setAuthorityStrategy(authorityStrategy());
		authenticationProvider.setBase64EncodedSecret(this.base64EncodedSecret);
		authenticationProvider.setSigningAlgorithm(Algorithm.valueOf(this.signingAlgorithm));
		authenticationProvider.setPublicKeyPath(this.publicKeyPath);
		return authenticationProvider;
	}

	@Bean(name = { "auth0EntryPoint" })
	public Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint() {
		return new Auth0AuthenticationEntryPoint();
	}

	@Bean(name = { "auth0Filter" })
	public Auth0AuthenticationFilter auth0AuthenticationFilter(Auth0AuthenticationEntryPoint entryPoint) {
		Auth0AuthenticationFilter filter = new Auth0AuthenticationFilter();
		filter.setEntryPoint(entryPoint);
		return filter;
	}

	@Bean(name = { "auth0AuthenticationFilterRegistration" })
	public FilterRegistrationBean auth0AuthenticationFilterRegistration(Auth0AuthenticationFilter filter) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(auth0AuthenticationProvider());
	}

	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, new String[] { "/**" });
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.addFilterAfter(auth0AuthenticationFilter(auth0AuthenticationEntryPoint()),
				SecurityContextPersistenceFilter.class)
				.addFilterBefore(simpleCORSFilter(), Auth0AuthenticationFilter.class);

		authorizeRequests(http);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@SuppressWarnings("rawtypes")
	protected void authorizeRequests(HttpSecurity http) throws Exception {
		((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http
				.authorizeRequests().antMatchers(new String[] { this.securedRoute })).authenticated()
						.antMatchers(new String[] { "/**" })).permitAll();
	}
}
