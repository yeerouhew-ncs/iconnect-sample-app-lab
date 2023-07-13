package com.ncs.iconnect.sample.lab.generated.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    private char[] password;

    private char[] tempAuthData;

    private Boolean rememberMe;

    @NotNull
    private String key;

    @NotNull
    private String iv;

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(char[] password) {
        this.password = password;
        this.tempAuthData = password;
    }

    public char[] getPassword() {
        return this.password;
    }

    public char[] getTempAuthData() {
        return tempAuthData;
    }

    public void setTempAuthData(char[] tempAuthData) {
        this.tempAuthData = tempAuthData;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}
