package com.ncs.iconnect.sample.lab.generated.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.to.User;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;


/**
 * Test class for the AccountResource REST controller.
 *
 * @see CustomerResource
 */
@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class AccountResourceIT {
	
	private MockMvc restAccountMockMvc;
	
	private MockMvc restAccountMockMvcForBranch;
	
	@Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

	@Autowired
    private TestTokenProvider testTokenProvider;
	
	private static final String LOGIN_USER_NAME = "appadmin";

	private static final String LOGIN_PASSWORD = "password1";
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }

	@BeforeEach
    public void setup() {
		MockitoAnnotations.initMocks(this);
		restAccountMockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
		restAccountMockMvcForBranch= MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
    public void isAuthenticated() throws Exception {
		restAccountMockMvc.perform(get("/api/authenticate").with(httpBasic(LOGIN_USER_NAME,LOGIN_PASSWORD)))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
    public void getAccount() throws Exception {
		restAccountMockMvc.perform(get("/api/account")
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
    public void getAccountWithoutAuthority() throws Exception {
		restAccountMockMvc.perform(get("/api/account"))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void getAccountWithoutHeader() throws Exception {
		restAccountMockMvcForBranch.perform(get("/api/logout"))
		.andDo(print())
		.andExpect(status().isOk());
		
		Authentication auth = new UsernamePasswordAuthenticationToken(LOGIN_USER_NAME,LOGIN_PASSWORD);
		SecurityContextHolder.getContext().setAuthentication(auth);
		restAccountMockMvcForBranch.perform(get("/api/account"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void getAccountContainsSlash() throws Exception {
		restAccountMockMvcForBranch.perform(get("/api/logout"))
		.andDo(print())
		.andExpect(status().isOk());
		
		Authentication auth = new UsernamePasswordAuthenticationToken(ITrustConstants.AUTH_METHOD_PASS + "/" +LOGIN_USER_NAME,LOGIN_PASSWORD);
		SecurityContextHolder.getContext().setAuthentication(auth);
		restAccountMockMvcForBranch.perform(get("/api/account"))
		.andDo(print())
		.andExpect(status().isOk());
	}
}
