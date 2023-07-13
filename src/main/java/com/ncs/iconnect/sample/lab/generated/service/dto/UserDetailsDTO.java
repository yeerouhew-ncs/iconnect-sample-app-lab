package com.ncs.iconnect.sample.lab.generated.service.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ncs.iframe5.component.menu.MenuTO;

public class UserDetailsDTO {
    private String subjectId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String status;
    private String username;
    private Date effectiveDate;
    private Date expireDate;

    private Set<String> authorities;
    private Collection<String> webURIs;
    private Collection<String> groups;
    private Collection<MenuTO> menus;

    private String authMethod;


    private String langKey = "en";

    private final Logger log = LoggerFactory.getLogger(UserDetailsDTO.class);

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

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAccountNonExpired() {
        if((this.effectiveDate == null || this.effectiveDate.before(new Date()))
            && (this.expireDate == null || this.expireDate.after(new Date()))){
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
        return arrayList;
    }

    public void setWebURIs(Collection<String> webURIs) {
        if (webURIs == null) {
            this.webURIs = null;
        } else {
            ArrayList<String> arrayList = new ArrayList<>(webURIs);
            this.webURIs =  arrayList;
        }
    }

    public Collection<String> getGroups() {
        if (groups == null) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>(groups);
        return arrayList;
    }

    public void setGroups(Collection<String> groups) {
        if (groups == null) {
            this.groups = null;
        } else {
            ArrayList<String> arrayList = new ArrayList<>(groups);
            this.groups =  arrayList;
        }
    }

    public Collection<MenuTO> getMenus() {
        if (menus == null) {
            return null;
        }
        ArrayList<MenuTO> menuList = new ArrayList<MenuTO>();
        for (MenuTO source : menus) {
			MenuTO target = new MenuTO();
			try {
				BeanUtils.copyProperties(target, source);
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.error("getMenus error:" + e.getMessage(),e);
			}
			menuList.add(target);
		}
        return menuList;
    }

    public void setMenus(Collection<MenuTO> menus) {
        if (menus == null) {
            this.menus = null;
        } else {
            this.menus = new ArrayList<MenuTO>();
        	for (MenuTO source : menus) {
        		MenuTO target = new MenuTO();
    			try {
    				BeanUtils.copyProperties(target, source);
    			} catch (IllegalAccessException | InvocationTargetException e) {
    				log.error("setMenus error:" + e.getMessage(),e);
    			}
    			this.menus.add(target);
			}
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

}
