package com.ncs.iconnect.sample.lab.generated.web.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ncs.itrust5.ss5.ITrustConstants.TokenType;
import com.ncs.itrust5.ss5.tools.SecurityUtils;
import com.ncs.iconnect.sample.lab.generated.security.jwt.JWTFilter;
import com.ncs.itrust5.ss5.jwt.TokenProvider;
import com.ncs.iconnect.sample.lab.generated.service.dto.UserDetailsDTO;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.UnauthenticatedException;
import com.ncs.iconnect.sample.lab.generated.security.jwt.JWTConfigurer;
import com.ncs.iframe5.commons.AppConfig;
import com.ncs.itrust5.ss5.service.AuthorityService;
import com.ncs.itrust5.ss5.service.UserService;
import com.ncs.itrust5.ss5.service.UserTokenService;
import com.ncs.itrust5.ss5.to.User;
import com.ncs.itrust5.ss5.tools.SecurityUtils;
import com.ncs.itrust5.ss5.tools.Tools;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AppConfig appConfig;

	@Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private Environment env;

    @Autowired
    private UserTokenService userTokenService;

    private static final String SESSION_TIME_OUT = "1800";

    private static final String IDLE_TIME = "300";

    
    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 401 if user is not authenticated
     */
    @GetMapping("/account")
    public UserDetailsDTO getAccount(HttpServletRequest request, HttpServletResponse response) {
    	if(SecurityUtils.isAuthenticated()){
    		String loginUserName = SecurityUtils.getCurrentUserLogin().get();
    		String authMethod = SecurityUtils.getCurrentUserAuthMethod();
    		String loginName = loginUserName;
    		if(loginUserName.contains("/")){
    			String[] authMethodAndName = loginUserName.split("/");
    			if(authMethodAndName.length == 2){
    				authMethod = authMethodAndName[0];
    				loginName = authMethodAndName[1];
    			}
    		}
    		UserDetails userDetails = userService.loadUserByUsernameAndLoginMethod(loginName, authMethod);
            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
    		if(userDetails instanceof com.ncs.itrust5.ss5.to.User){
                User user = (User) userDetails;
                try {
                    PropertyUtils.copyProperties(userDetailsDTO, user);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

    			String subjectId = userDetailsDTO.getSubjectId();
    			String appId = this.authorityService.getAppIdByAppCode(this.appConfig.getAppCode());
        		Collection<GrantedAuthority> grantedAuthorities = authorityService.getGrantedAuthorityBySubjectId(subjectId, appId);
        		Collection<String> groups = authorityService.getAllGroupsByUsername(userDetailsDTO.getUsername());
        		userDetailsDTO.setGroups(groups);
                Set<String> authorities = new HashSet<>();
                authorities.add("ROLE_USER"); //set default role.
                List<String> roleIds = new ArrayList<>();
                if (grantedAuthorities != null) {
                    for (GrantedAuthority authority : grantedAuthorities) {
                        authorities.add(authority.getAuthority());
                        roleIds.add(authority.getAuthority());
                    }
                }
                userDetailsDTO.setAuthorities(authorities);
                userDetailsDTO.setAuthMethod(authMethod);

                userDetailsDTO.setMenus(authorityService.getUserMenu(roleIds));
        		List<String> menuURLs = new ArrayList<String>();
        		Tools.extractMenuURL((List)userDetailsDTO.getMenus(),menuURLs);
        		List<String> weburis = authorityService.getGrantedWeburiByRoleIds(roleIds);
        		weburis.addAll(menuURLs);
                userDetailsDTO.setWebURIs(new HashSet<>(weburis));
    		}
    		return userDetailsDTO;
    	}else{
    		throw new UnauthenticatedException("User could not be found");
    	}
    }

    @GetMapping("/logout")
    public void logout() {
        try {
            log.debug("Logout uer: " + SecurityUtils.getCurrentUserLoginMethodAndUserId());
            userTokenService.deleteUserSessionToken(SecurityUtils.getCurrentUserLoginMethodAndUserId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        SecurityContextHolder.clearContext();
    }

    @GetMapping("/session-timeout-min")
    public String[] getSessionTimeoutMin() {
        String[] timeoutCouple = {SESSION_TIME_OUT,IDLE_TIME};
        String sessionTimeout = env.getProperty("server.servlet.timeout");
        String remainAlertTime = env.getProperty("server.servlet.remain-time-alert");
        if(StringUtils.isNotEmpty(sessionTimeout)){
            timeoutCouple[0] = sessionTimeout;
        }
        if(StringUtils.isNotEmpty(remainAlertTime)){
            timeoutCouple[1] = remainAlertTime;
        }
        return timeoutCouple;
    }
}