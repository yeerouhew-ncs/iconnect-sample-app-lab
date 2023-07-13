package com.ncs.iconnect.sample.lab.generated.security.jwt;

import com.ncs.iconnect.sample.lab.generated.security.AuthoritiesConstants;
import com.ncs.itrust5.ss5.service.UserService;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.domain.SubjectLogin;
import com.ncs.itrust5.ss5.domain.UserToken;
import com.ncs.itrust5.ss5.service.AuditEventService;
import com.ncs.itrust5.ss5.service.UserTokenBuilder;
import com.ncs.itrust5.ss5.service.UserTokenService;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import com.ncs.itrust5.ss5.jwt.TokenProvider;

public class JWTFilterTest {

    private TokenProvider tokenProvider;

    private JWTFilter jwtFilter;
    private UserService userService;
    private AuditEventService auditEventService;
    private UserTokenService userTokenService;
    private UserTokenBuilder userTokenBuilder;

    @BeforeEach
    public void setup() {
        JHipsterProperties jHipsterProperties = new JHipsterProperties();
        userService = Mockito.mock(UserService.class);
        auditEventService = Mockito.mock(AuditEventService.class);
        userTokenService = Mockito.mock(UserTokenService.class);
        userTokenBuilder = Mockito.mock(UserTokenBuilder.class);
        tokenProvider = new TokenProvider(jHipsterProperties, userService, auditEventService, userTokenService, userTokenBuilder);
        ReflectionTestUtils.setField(tokenProvider, "key",
            Keys.hmacShaKeyFor(Decoders.BASE64
                .decode("fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8")));

        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", 60000);
        jwtFilter = new JWTFilter(tokenProvider);
        SecurityContextHolder.getContext().setAuthentication(null);
        Mockito.when(userService.getSubjectLoginByUsernameAndLoginMethod("", "")).thenReturn(getSubjectLogin());
    }

	private SubjectLogin getSubjectLogin() {
		SubjectLogin subjectLogin = new SubjectLogin();
		subjectLogin.setSubjectId("12345678");
		return subjectLogin;
	}

	@Test
    public void testJWTFilter() throws Exception {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "test-user",
            "test-password",
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        String jwt = tokenProvider.createToken(authentication, false);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        when(this.userTokenBuilder.buildToken(anyString(), any(), anyString())).thenReturn(newUserToken());
        when(userTokenService.existsUserSessionToken(anyString(), anyString())).thenReturn(true);
        when(userTokenService.existsUserToken(anyString(), any(),anyString())).thenReturn(true);
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("PASSWORD/test-user");
        assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()).isEqualTo(jwt);
    }

    @Test
    public void testJWTFilterInvalidToken() throws Exception {
        String jwt = "wrong_jwt";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testJWTFilterMissingAuthorization() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testJWTFilterMissingToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer ");
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testJWTFilterWrongScheme() throws Exception {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "test-user",
            "test-password",
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        String jwt = tokenProvider.createToken(authentication, false);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Basic " + jwt);
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private UserToken newUserToken() {
        UserToken userToken = new UserToken();
        return userToken;
    }
}