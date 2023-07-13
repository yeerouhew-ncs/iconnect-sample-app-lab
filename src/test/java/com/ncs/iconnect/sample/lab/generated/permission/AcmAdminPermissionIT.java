package com.ncs.iconnect.sample.lab.generated.permission;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class AcmAdminPermissionIT  {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private TestTokenProvider testTokenProvider;

	private MockMvc mockMvc;
    private MediaType mediaType = MediaType.APPLICATION_JSON;

    private static final String CODE_ADMIN_WEBURI = "admin/ic-codetype";
    private static final String AUDIT_ADMIN_WEBURI = "admin/ic-entityaudit";
    private static final String USER_ADMIN_WEBURI = "admin/acm/ic-user";
    private static final String GROUP_ADMIN_WEBURI = "admin/acm/ic-group";
    private static final String RESOURCE_ADMIN_WEBURI = "admin/acm/ic-resource";
    private static final String APPLICATION_WEBURI = "admin/acm/ic-application";
    private static final String FUNCTION_ADMIN_WEBURI = "admin/acm/ic-function";
    private static final String ROLE_ADMIN_WEBURI = "admin/acm/ic-role";
    private static final String PARAMETER_ADMIN_WEBURI = "admin/ic-param";
    
    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}

	//User
	@Test
    public void testUserAdminFuncitonWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(USER_ADMIN_WEBURI)));
    }
	
	@Test
    public void testUserAdminFuncitonWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	//Group
	@Test
    public void testGroupWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(GROUP_ADMIN_WEBURI)));
    }
	
	@Test
    public void testGroupAdminFuncitonWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
        
        result.andDo(print())
        	.andExpect(status().isUnauthorized());
    }
	
	//Resource
	@Test
    public void testResourceResourceWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(RESOURCE_ADMIN_WEBURI)));
    }
	
	@Test
    public void testResourceResourceWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
        
        result.andDo(print())
	        .andExpect(status().isUnauthorized());
    }
	
	//Application
	@Test
    public void testApplicationFuncitonWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.webURIs").value(hasItem(APPLICATION_WEBURI)));
    }
    
    @Test
    public void testApplicationFuncitonWebURIAccessFailure() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	//Function
    @Test
    public void testFuncitonWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(FUNCTION_ADMIN_WEBURI)));
    }
	
	@Test
    public void testFunctionAdminFuncitonWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyUser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	//Role
	@Test
    public void testRoleWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(ROLE_ADMIN_WEBURI)));
    }
	
	@Test
    public void testRoleAdminFuncitonWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyUser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	//Code Table Admin
	@Test
    public void testCodeAdminFuncitonWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.webURIs").value(hasItem(CODE_ADMIN_WEBURI)));
    }
	
    @Test
    public void testCodeAdminFuncitonWebURIAccessFailure() throws Exception{
    	
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    //Audit Admin
    @Test
    public void testAuditAdminFuncitonWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(AUDIT_ADMIN_WEBURI)));
    }
	
	@Test
    public void testAuditAdminFuncitonWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumhyuser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	//parameter Admin
	@Test
    public void testPrarmeterAdminFuncitonWebURIAccessSuccess() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.webURIs").value(hasItem(PARAMETER_ADMIN_WEBURI)));
    }
	
	@Test
    public void testPrarmeterAdminFuncitonWebURIAccessUnAuthorized() throws Exception{
        ResultActions result = mockMvc.perform(get("/api/account")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyUser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	@Test
	public void testAccountResourcelogout() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/logout")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testSessionTimeoutMin() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/session-timeout-min")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
    public void testAuthenticationException() throws Exception{
        
        ResultActions result = mockMvc.perform(get("/api/authenticate")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        
        result.andDo(print())
	        .andExpect(status().isOk()).andReturn().equals("appadmin");
    }

}
