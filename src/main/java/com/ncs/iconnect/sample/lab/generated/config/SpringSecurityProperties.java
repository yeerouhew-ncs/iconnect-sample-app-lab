package com.ncs.iconnect.sample.lab.generated.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring-security")
public class SpringSecurityProperties {
	
	private String realmName;
	
	private String eraseCredentialsAfterAuthentication;
	
	private String hideUserNotFoundExceptions;

	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	public String getEraseCredentialsAfterAuthentication() {
		return eraseCredentialsAfterAuthentication;
	}

	public void setEraseCredentialsAfterAuthentication(String eraseCredentialsAfterAuthentication) {
		this.eraseCredentialsAfterAuthentication = eraseCredentialsAfterAuthentication;
	}

	public String getHideUserNotFoundExceptions() {
		return hideUserNotFoundExceptions;
	}

	public void setHideUserNotFoundExceptions(String hideUserNotFoundExceptions) {
		this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
	}
	
	
	
	

}
