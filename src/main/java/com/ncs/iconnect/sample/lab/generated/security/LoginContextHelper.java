package com.ncs.iconnect.sample.lab.generated.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import com.ncs.itrust5.ss5.jwt.TokenPayload;
/**
 * Help class for Login context.
 */
@Component
public class LoginContextHelper {
    private final Logger log = LoggerFactory.getLogger(LoginContextHelper.class);

    public String getCurrentUserJwtToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) securityContext.getAuthentication();
        return String.valueOf(authenticationToken.getCredentials());
    }

    public Optional<TokenPayload> getCurrentUserTokenPayload() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getDetails() instanceof TokenPayload)
            .map(authentication -> (TokenPayload) authentication.getDetails());
    }

    /**
     * Get UUID of login user
     *
     * @return UUID of login user
     */
    public String getCurrentUserUUID() {
        Optional<TokenPayload> tokenPayloadOption = getCurrentUserTokenPayload();
        if (tokenPayloadOption.isPresent()) {
            return tokenPayloadOption.get().getSubjectUUID();
        } else {
            log.warn("TokenPayloadOption not present, use user login id instead...");
            return null;
        }
    }

    private Collection<? extends GrantedAuthority> getCurrentUserGrantedAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getAuthorities();
    }

    public boolean isLoginUserHasRole(String roleId) {
        Collection<? extends GrantedAuthority> authrorites = getCurrentUserGrantedAuthorities();
        return authrorites.stream().anyMatch(authority -> StringUtils.equalsIgnoreCase(authority.getAuthority(), roleId));
    }

}
