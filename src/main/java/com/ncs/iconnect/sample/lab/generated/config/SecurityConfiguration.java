package com.ncs.iconnect.sample.lab.generated.config;

import com.ncs.iconnect.sample.lab.generated.security.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import com.ncs.iconnect.sample.lab.generated.security.jwt.JWTFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import com.ncs.itrust5.ss5.filter.CSRFFilter;
import com.ncs.itrust5.ss5.service.AuthorityService;
import com.ncs.itrust5.ss5.ITrustConfig;
import com.ncs.iframe5.commons.AppConfig;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import com.ncs.itrust5.ss5.authentication.www.AdvBasicAuthenticationEntryPointNoPopup;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.annotation.Resource;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import com.ncs.itrust5.ss5.authentication.DBAuthenticationProvider;
import com.ncs.itrust5.ss5.service.UserService;
import com.ncs.iframe5.commons.util.IFrameBeans;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import com.ncs.itrust5.ss5.authentication.LDAPAuthenticationProvider;
import com.ncs.itrust5.ss5.jwt.TokenProvider;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import org.springframework.core.env.Environment;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
	
	@Autowired
    private Environment env;
	
	@Autowired
	private SpringSecurityProperties properties;
	
	@Autowired
	@Qualifier("filterSecurityInterceptor")
	private FilterSecurityInterceptor filterSecurityInterceptor;
	
	@Autowired
	@Qualifier("openEntityManagerInViewFilter")
	private OpenEntityManagerInViewFilter openEntityManagerInViewFilter;
	
	@Autowired
	@Qualifier("jwtFilter")
	private JWTFilter jwtFilter;
	
	@Autowired
	@Qualifier("CSRFFilter")
	private CSRFFilter cSRFFilter;
	
	@Autowired
	@Qualifier("httpSessionCsrfTokenRepository")
	private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;
	
	@Autowired
	@Qualifier("authenticationEntryPoint")
	private AdvBasicAuthenticationEntryPointNoPopup authenticationEntryPoint;
	
	@Resource
	@Qualifier("logoutSuccessHandler")
	private HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler;
	
	@Resource(name="userDetailsService")
	private UserDetailsService userDetailsService;
	
	@Resource(name="passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Resource(name="authorityService")
	private AuthorityService authorityService;
	
	@Resource(name="appConfig")
	private AppConfig appConfig;
	
	@Resource(name="itrustConfig")
	private ITrustConfig itrustConfig;

	private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(TokenProvider tokenProvider, CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return authenticationManager();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		log.debug("config security configuration");
		//For stateless application, CSRF token is not required as application does not need JSessionID in Cookie 
		//Application use JWT token for each request, CSRF attack not possible
		//Warning: Enable CSRF and stateless at same time lead to random 403 error under heavy load
        // @formatter:off
        http
            .authorizeRequests().antMatchers(HttpMethod.TRACE, "**").denyAll()
            .and()
            .csrf().disable()
            .addFilterBefore(filterSecurityInterceptor,FilterSecurityInterceptor.class)
            .addFilterBefore(openEntityManagerInViewFilter, SecurityContextPersistenceFilter.class)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(cSRFFilter, CsrfFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(problemSupport)
            .and()
            .logout().logoutSuccessHandler(logoutSuccessHandler)
            .and()
            .headers()
            .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
            .and()
            .frameOptions()
            .deny()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
	}

	@Override
	protected AuthenticationManager authenticationManager() {
		List<AuthenticationProvider> providerList = new ArrayList<>();
		String provider = env.getProperty("iconnect.itrust.provider");

		if("db".equals(provider)){
			providerList.add(dBAuthenticationProvider());
		}
		if("ldap".equals(provider)){
			providerList.add(lDAPAuthenticationProvider());
		}
		ProviderManager authenticationManager = new ProviderManager(providerList);
		authenticationManager.setEraseCredentialsAfterAuthentication(true);
		return authenticationManager;
	}

	@Bean
	DBAuthenticationProvider dBAuthenticationProvider(){
		
		DBAuthenticationProvider dBAuthenticationProvider = new DBAuthenticationProvider();
		dBAuthenticationProvider.setUserDetailsService(userDetailsService);
		dBAuthenticationProvider.setUserService((UserService)userDetailsService);
		dBAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		dBAuthenticationProvider.setAuthorityService(authorityService);
		dBAuthenticationProvider.setAppConfig(appConfig);
		dBAuthenticationProvider.setItrustConfig(itrustConfig);
		return dBAuthenticationProvider;
	}
	@Bean
	LDAPAuthenticationProvider lDAPAuthenticationProvider(){

		LDAPAuthenticationProvider lDAPAuthenticationProvider = new LDAPAuthenticationProvider();
		lDAPAuthenticationProvider.setUserService((UserService)userDetailsService);
		lDAPAuthenticationProvider.setAuthorityService(authorityService);
		lDAPAuthenticationProvider.setAppConfig(appConfig);
		lDAPAuthenticationProvider.setLDAPLoginProvideURL(env.getProperty("ldap.ldapLoginProvideURL"));
		lDAPAuthenticationProvider.setSecurityCredential(env.getProperty("ldap.securityCredential"));
		lDAPAuthenticationProvider.setSecurityPrincipal(env.getProperty("ldap.securityPrincipal"));
		lDAPAuthenticationProvider.setAutoProvision("true".equals(env.getProperty("ldap.autoProvision"))?true:false);
		lDAPAuthenticationProvider.setSearchName(env.getProperty("ldap.searchName"));
		lDAPAuthenticationProvider.setSearchFilter(env.getProperty("ldap.searchFilter"));
		lDAPAuthenticationProvider.setLdapAttrMapFirstName(env.getProperty("ldap.ldapAttrMapFirstName"));
		lDAPAuthenticationProvider.setLdapAttrMapLastName(env.getProperty("ldap.ldapAttrMapLastName"));
		lDAPAuthenticationProvider.setLdapAttrMapEmail(env.getProperty("ldap.ldapAttrMapEmail"));
		lDAPAuthenticationProvider.setHideUserNotFoundExceptions("true".equals(env.getProperty("ldap.hideUserNotFoundExceptions"))?true:false);
		return lDAPAuthenticationProvider;
	}

	@Bean
	IFrameBeans iFrameBeans(){
		return new IFrameBeans();
	}
	
	@Bean(name="logoutSuccessHandler")
	public HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler(){
		HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler = new HttpStatusReturningLogoutSuccessHandler();
		return logoutSuccessHandler;
	}
	
	@Bean(name="authenticationEntryPoint")
	public AdvBasicAuthenticationEntryPointNoPopup authenticationEntryPoint(){
		
		AdvBasicAuthenticationEntryPointNoPopup authenticationEntryPoint = new AdvBasicAuthenticationEntryPointNoPopup();
		authenticationEntryPoint.setRealmName(properties.getRealmName());
		return authenticationEntryPoint;
	}

	private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

}
