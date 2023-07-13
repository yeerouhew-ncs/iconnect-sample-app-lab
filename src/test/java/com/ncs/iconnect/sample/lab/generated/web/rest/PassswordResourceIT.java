package com.ncs.iconnect.sample.lab.generated.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.itrust5.rest.response.ChangePasswordRequest;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.domain.SubjectLogin;
import com.ncs.itrust5.ss5.domain.SubjectPasswordHistory;
import com.ncs.itrust5.ss5.service.SubjectPasswordHistoryService;
import com.ncs.itrust5.ss5.service.UserService;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class PassswordResourceIT {

    @Mock
    private HttpServletResponse response;
    
    @Autowired
    private UserService userService;
	@Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private TestTokenProvider testTokenProvider;
	@Autowired
	private SubjectPasswordHistoryService subjectPasswordHistoryService;
	@Autowired
	private PasswordEncoder passwordEncoder;
    
	private MockMvc restAccountMockMvc;
	
	private static final String INITIAL_PASSWORK_URL = "/api/itrust/initPassword";
	private static final String CHANGE_PASSWORK_URL = "/api/itrust/password";
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
	
	@BeforeEach
    public void setup() {
		MockitoAnnotations.initMocks(this);
		restAccountMockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}
	
	private void resetPassword(String loginname) {
		SubjectLogin subjectLogin = userService.getSubjectLoginByUsernameAndLoginMethod(loginname, ITrustConstants.AUTH_METHOD_PASS);
		subjectLogin.setPassword(passwordEncoder.encode("password1"));
		userService.updateSubjectLogin(subjectLogin);
	}
	
	private ChangePasswordRequest getRequestData() {
		ChangePasswordRequest request = new ChangePasswordRequest();
		request.setUsername("appadmin");
		request.setNewPassword("ChangeIt!@#");
		request.setNewPassword2("ChangeIt!@#");
		request.setOldPassword("ChangeIt!@#");
		return request;
	}
	
	@Test
	public void testChangePasswordAfterLogin_NewPasswordIsnotMatchWithOld() throws Exception {
		ChangePasswordRequest request = getRequestData();
		request.setNewPassword2("ChangeIt!@#123");
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post("/api/itrust/initPassword")
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "The 2 new passwords is not same."));
	}
	
	@Test
	public void testChangePasswordAfterLogin_RecallQuestionIsNull() throws Exception {
		ChangePasswordRequest request = getRequestData();
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(INITIAL_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "Recall question and answer should both empty or not empty."));
	}
	
	@Test
	public void testChangePasswordAfterLogin_PasswordIdentical() throws Exception {
		ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("same password");
		request.setNewPassword("same password");
		request.setNewPassword2("same password");
		
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(INITIAL_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "New password is same with old password."));
	}
	
	@Test
	public void testChangePasswordAfterLogin_oldpasswordnotcorrect() throws Exception {
		ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("same password");
		request.setNewPassword("same password1");
		request.setNewPassword2("same password1");
		
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(INITIAL_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "Your old password is not correct."));
	}
	
	@Test
	public void testChangePasswordAfterLogin_newpasswordsamewithID() throws Exception {
		
		ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("password1");
		request.setNewPassword("appadmin");
		request.setNewPassword2("appadmin");
		
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(INITIAL_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "Your new password is same with userId."));
	}
	
	@Test
	public void testChangePasswordAfterLogin_newpasswordexitsInPasswordHis() throws Exception {
		
		ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("password1");
		request.setNewPassword("Password!11");
		request.setNewPassword2("Password!11");
		
		response.addHeader("username", "appadmin");
		
		SubjectPasswordHistory passhis = new SubjectPasswordHistory();
		passhis.setSubjectId("DEF-user-appadmin");
		passhis.setOldPassword(passwordEncoder.encode(request.getNewPassword()));
		passhis.setCreatedDt(new Timestamp(System.currentTimeMillis()));
		passhis.setLoginName(request.getUsername());
		subjectPasswordHistoryService.save(passhis);
		
        ResultActions result = restAccountMockMvc.perform(post(INITIAL_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "New password is same with history password."));
	}
	
	@Test
	public void testChangePasswordAfterLogin_passwordChanged() throws Exception {
		
		ChangePasswordRequest request = getRequestData();
		
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("password1");
		request.setNewPassword("same password1");
		request.setNewPassword2("same password1");
		
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(INITIAL_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(200))
        	.andExpect(jsonPath("$.msg").value("Password was changed."))
        	.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					resetPassword(request.getUsername());
				}
	   		});
	}
	
	


	@Test
	public void testChangePasswordOnLogin_2passworknotsame() throws IOException, Exception {
		ChangePasswordRequest request = getRequestData();
		request.setNewPassword2("ChangeIt!@#123");
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "The 2 new passwords is not same."));
	}
	
	@Test
	public void testChangePasswordOnLogin_PasswordIdentical() throws IOException, Exception {
		ChangePasswordRequest request = getRequestData();
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "New password is same with old password."));
	}
	
	
	@Test
	public void testChangePasswordOnLogin_RecallQuestionIsNull() throws Exception {
		ChangePasswordRequest request = getRequestData();
		response.addHeader("username", "appadmin");
		request.setRecallAnswer("answer");
		request.setRecallQuestion("");
		request.setNewPassword("123456");
		request.setNewPassword2("123456");
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "Recall question and answer should both empty or not empty."));
	}
	
	@Test
	public void testChangePasswordOnLogin_passwordsamwithID() throws Exception {
		ChangePasswordRequest request = getRequestData();
		response.addHeader("username", "appadmin");
		request.setRecallAnswer("answer");
		request.setRecallQuestion("");
		request.setNewPassword("appadmin");
		request.setNewPassword2("appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "Recall question and answer should both empty or not empty."));
	}
	
	@Test
	public void testChangePasswordOnLogin_passworkexitsInPassworkhistory() throws Exception {
        
        ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("password1");
		request.setNewPassword("Password!11");
		request.setNewPassword2("Password!11");
		
		response.addHeader("username", "appadmin");
		
		SubjectPasswordHistory passhis = new SubjectPasswordHistory();
		passhis.setSubjectId("DEF-user-appadmin");
		passhis.setOldPassword(passwordEncoder.encode(request.getNewPassword()));
		passhis.setCreatedDt(new Timestamp(System.currentTimeMillis()));
		passhis.setLoginName(request.getUsername());
		subjectPasswordHistoryService.save(passhis);
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "New password is same with history password."));
	}
	
	@Test
	public void testChangePasswordOnLogin_oldpasswordnotcorrect() throws Exception {
		ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("same password");
		request.setNewPassword("same password1");
		request.setNewPassword2("same password1");
		
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(900))
        	.andExpect(header().string("error", "Your old password is not correct."));
	}
	
	@Test
	public void testChangePasswordOnLogin_passwordChanged() throws Exception {
		ChangePasswordRequest request = getRequestData();
		request.setRecallAnswer("test");
		request.setRecallQuestion("test");
		request.setOldPassword("password1");
		request.setNewPassword("same password1");
		request.setNewPassword2("same password1");
		
		response.addHeader("username", "appadmin");
		
        ResultActions result = restAccountMockMvc.perform(post(CHANGE_PASSWORK_URL)
        		.content(TestUtil.convertObjectToJsonBytes(request))
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().is(200))
        	.andExpect(jsonPath("$.msg").value("Password was changed."))
        	.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					resetPassword(request.getUsername());
				}
	   		});
	}
}