package com.ncs.iconnect.sample.lab.generated.security;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.ncs.iconnect.sample.lab.generated.web.rest.vm.LoginVM;
import com.ncs.iframe5.commons.AppConfig;

import com.ncs.itrust5.ss5.service.AuthorityService;
import com.ncs.itrust5.ss5.jwt.TokenPayload;
import com.ncs.itrust5.ss5.jwt.TokenProvider;

@Service
public class TestTokenProvider {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AppConfig appConfig;
    public String createTestJWTToken(String username, String loginSubjectId,
                                            String password) throws Exception {
        LoginVM loginVm = new LoginVM();
        loginVm.setUsername(username);
        loginVm.setPassword(password.toCharArray());
        TestUserDto user = new TestUserDto();
        user.setAuthMethod("PASSWORD");
        user.setSubjectId(loginSubjectId);
        user.setUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
            new String(loginVm.getPassword())
            ,getGrantedAuthority(loginSubjectId));

        authenticationToken.setDetails(getTokenPayload(loginSubjectId));
        return tokenProvider.createTokenWithSubjectUUID(authenticationToken, loginSubjectId, true);
    }

	private static TokenPayload getTokenPayload(String loginUserUUID) {
		TokenPayload tokeanPayload = new TokenPayload();
		return tokeanPayload.subjectUUID(loginUserUUID);
	}

    private Collection<GrantedAuthority> getGrantedAuthority(String subjectId){
		String appId = this.authorityService.getAppIdByAppCode(appConfig.getAppCode());
		Collection<GrantedAuthority> authorities = authorityService.getGrantedAuthorityBySubjectId(subjectId,
				appId);
		return authorities;
	}
}
