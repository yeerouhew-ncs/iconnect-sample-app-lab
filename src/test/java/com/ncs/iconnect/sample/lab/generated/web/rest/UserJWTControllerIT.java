package com.ncs.iconnect.sample.lab.generated.web.rest;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ncs.iconnect.sample.lab.generated.web.rest.vm.TokenVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.itrust5.ss5.jwt.TokenProvider;
import com.ncs.iconnect.sample.lab.generated.web.rest.vm.LoginVM;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.service.UserTokenBuilder;
import com.ncs.itrust5.ss5.service.UserTokenService;
import com.ncs.itrust5.ss5.tools.AESUtil;
import java.security.GeneralSecurityException;
import java.io.UnsupportedEncodingException;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class UserJWTControllerIT {
	
	private MockMvc restUserJWTMockMvc;
	
	@Mock
	private TokenProvider tokenProvider;
	
	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserTokenService userTokenService;
	
	@Mock
	private UserTokenBuilder userTokenBuilder;
	
	private static final String LOGIN_USER_NAME = "appadmin";
	private static final String LOGIN_PASSWORD = "password1";
	private static final String LOGIN_KEY = "FDFJEJF3f332b4DD";
	private static final String DUMMY_JWT = "FDFJEJF3f332b";
	private static final String LOGIN_IV = "ABCJEJF3f33BNMDD";
	
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		UserJWTController userJWTController = new UserJWTController(tokenProvider, authenticationManager, userTokenService);
		this.restUserJWTMockMvc = MockMvcBuilders.standaloneSetup(userJWTController).build();
	}
	
	@Test
	public void authorize() throws Exception {
		LoginVM loginVM = new  LoginVM();
		loginVM.setUsername(LOGIN_USER_NAME);
		loginVM.setPassword(LOGIN_PASSWORD.toCharArray());

		loginVM.setKey(LOGIN_KEY);
		loginVM.setIv(LOGIN_IV);
		loginVM.setRememberMe(false);

		encryptLoginVM(loginVM);

		Authentication auth = new UsernamePasswordAuthenticationToken(ITrustConstants.AUTH_METHOD_PASS + "/" +LOGIN_USER_NAME,LOGIN_PASSWORD);
		SecurityContextHolder.getContext().setAuthentication(auth);
		when(authenticationManager.authenticate(anyObject())).thenReturn(auth);
		when(tokenProvider.createToken(anyObject(), anyBoolean())).thenReturn(DUMMY_JWT);
		when(userTokenService.existsUserSession(anyObject())).thenReturn(false);
		restUserJWTMockMvc.perform(post("/api/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(loginVM)))
				.andExpect(status().isOk());
		
	}

	@Test
    public void createAccessToken() throws Exception {
        TokenVM vm = new TokenVM();
        vm.setExpireDateAsStr("2099-11-29");

        Authentication auth = new UsernamePasswordAuthenticationToken(ITrustConstants.AUTH_METHOD_PASS + "/" +LOGIN_USER_NAME,LOGIN_PASSWORD);
        SecurityContextHolder.getContext().setAuthentication(auth);
        restUserJWTMockMvc.perform(post("/api/accessToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vm)))
            .andExpect(status().is5xxServerError());
    }

	private void encryptLoginVM(LoginVM loginVM) throws UnsupportedEncodingException, GeneralSecurityException {
    	String userName = loginVM.getUsername();
		String password = String.valueOf(loginVM.getPassword());
		String key = loginVM.getKey();
		String iv = loginVM.getIv();
		loginVM.setUsername(AESUtil.encrypt(userName, key, iv));
		loginVM.setPassword(AESUtil.encrypt(password, key, iv).toCharArray());
    }
}
