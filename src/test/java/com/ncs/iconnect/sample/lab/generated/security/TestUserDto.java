package com.ncs.iconnect.sample.lab.generated.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Test class that replaces com.ncs.itrust5.ss5.to.User to remove dependency on itrust
 */
public class TestUserDto implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String subjectId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNum;
	private String status;
	private String username;
    private char[] password = new char[] {};
	private Date effectiveDate;
	private Date expireDate;
	
	private Collection<GrantedAuthority> authorities;
	private Collection<String> webURIs;
	
	private String authMethod;
	
	
	private String langKey = "en";
	

	
	public String getAuthMethod() {
		return authMethod;
	}

	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public void setPassword(String password) {
    	if(password==null) {
    		this.password = new char[] {};
    	}else {
    		this.password = password.toCharArray();
    	}
    }

    public String getPassword() {
        return new String(this.password);
    }
    

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
        if (authorities == null) {
            this.authorities = null;
        } else {
            ArrayList<GrantedAuthority> arrayList = new ArrayList<>(authorities);
            this.authorities =  (Collection<GrantedAuthority>)arrayList.clone();
        }
	}

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return null;
        }
        ArrayList<GrantedAuthority> arrayList = new ArrayList<>(authorities);
        return (Collection<GrantedAuthority>) arrayList.clone();
    }

	public String getUsername() {
		return this.username;
	}

	public boolean isAccountNonExpired() {
		if((this.effectiveDate == null || this.effectiveDate.getTime()<=System.currentTimeMillis())
			&& (this.expireDate == null || this.expireDate.getTime()>=System.currentTimeMillis())){
			return true;
		}else{
			return false;
		}
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return ("A".equals(this.status));
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Collection<String> getWebURIs() {
        if (webURIs == null) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>(webURIs);
        return (Collection<String>) arrayList.clone();
	}

	public void setWebURIs(Collection<String> webURIs) {
        if (webURIs == null) {
            this.webURIs = null;
        } else {
            ArrayList<String> arrayList = new ArrayList<>(webURIs);
            this.webURIs =  (Collection<String>)arrayList.clone();
        }
	}

	public Date getEffectiveDate() {
        return effectiveDate == null ? null : (Date) effectiveDate.clone();
	}

	public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate == null ? null : (Date) effectiveDate.clone();
	}

	public Date getExpireDate() {
        return expireDate == null ? null : (Date) expireDate.clone();
	}

	public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate == null ? null : (Date) expireDate.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subjectId == null) ? 0 : subjectId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestUserDto other = (TestUserDto) obj;
		if (subjectId == null) {
			if (other.subjectId != null)
				return false;
		} else if (!subjectId.equals(other.subjectId))
			return false;
		return true;
	}
	
	
	
}