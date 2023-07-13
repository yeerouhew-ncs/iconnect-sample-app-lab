package com.ncs.iconnect.sample.lab.generated.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import com.ncs.itrust5.ss5.service.UserTokenBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import com.ncs.iconnect.sample.lab.generated.security.AuthoritiesConstants;
import com.ncs.itrust5.ss5.service.UserService;
import com.ncs.itrust5.ss5.domain.SubjectLogin;
import com.ncs.itrust5.ss5.service.AuditEventService;
import com.ncs.itrust5.ss5.service.UserTokenService;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.ncs.itrust5.ss5.jwt.TokenProvider;

public class TokenProviderTest {

    private final Base64.Encoder encoder = Base64.getEncoder();
    private final long ONE_MINUTE = 60000;
    private Key key;
    private JHipsterProperties jHipsterProperties;
    private TokenProvider tokenProvider;
    private UserService userService;
    private AuditEventService auditEventService;
    private UserTokenService userTokenService;
    private UserTokenBuilder userTokenBuilder;

    @BeforeEach
    public void setup() {
        jHipsterProperties = Mockito.mock(JHipsterProperties.class);
        userService = Mockito.mock(UserService.class);
        auditEventService = Mockito.mock(AuditEventService.class);
        userTokenService = Mockito.mock(UserTokenService.class);
        userTokenBuilder = Mockito.mock(UserTokenBuilder.class);
        tokenProvider = new TokenProvider(jHipsterProperties, userService,auditEventService, userTokenService, userTokenBuilder);
        key = Keys.hmacShaKeyFor(Decoders.BASE64
            .decode("fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8"));

        ReflectionTestUtils.setField(tokenProvider, "key", key);
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", ONE_MINUTE);
        Mockito.when(userService.getSubjectLoginByUsernameAndLoginMethod("", "")).thenReturn(getSubjectLogin());
    }


	private SubjectLogin getSubjectLogin() {
		SubjectLogin subjectLogin = new SubjectLogin();
		subjectLogin.setSubjectId("12345678");
		return subjectLogin;
	}


	@Test
    public void testReturnFalseWhenJWThasInvalidSignature() {
        boolean isTokenValid = tokenProvider.validateToken(createTokenWithDifferentSignature());

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisMalformed() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        String invalidToken = token.substring(1);
        boolean isTokenValid = tokenProvider.validateToken(invalidToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisExpired() {
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", -ONE_MINUTE);

        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        boolean isTokenValid = tokenProvider.validateToken(token);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisUnsupported() {
        String unsupportedToken = createUnsupportedToken();

        boolean isTokenValid = tokenProvider.validateToken(unsupportedToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisInvalid() {
        boolean isTokenValid = tokenProvider.validateToken("");

        assertThat(isTokenValid).isEqualTo(false);
    }

    private Authentication createAuthentication() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
        return new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities);
    }

    private String createUnsupportedToken() {
        return Jwts.builder()
            .setPayload("payload")
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    private String createTokenWithDifferentSignature() {
        Key otherKey = Keys.hmacShaKeyFor(Decoders.BASE64
            .decode("Xfd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8"));

        return Jwts.builder()
            .setSubject("anonymous")
            .signWith(otherKey, SignatureAlgorithm.HS512)
            .setExpiration(new Date(new Date().getTime() + ONE_MINUTE))
            .compact();
    }
}