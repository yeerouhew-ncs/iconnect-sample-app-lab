package com.ncs.iconnect.sample.lab.generated.permission;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iforge5.logadmin.service.AuditLogService;
import com.ncs.iforge5.logadmin.to.DownloadFile;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class AuditPermissionIT {

	@Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

	private MockMvc mockMvc;
	
	@Mock
	private AuditLogService auditLogService;

	@Autowired
    private TestTokenProvider testTokenProvider;
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
	
	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
		
		MockitoAnnotations.initMocks(this);
	}

	
	@Test
    public void testGetAuditsWithAuthorizedUserExpectSuccess() throws Exception {

        // when
        ResultActions result = mockMvc.perform(get("/api/log/auditLog/?fromDateAsStr=&toDateAsStr=&revision=&userId=&revisionType=&funcName=&tableName=&businessKey=&page=&size=&sort=")
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        // then
        result.andDo(print())
                .andExpect(status().isOk());
    }
	
	@Test
    public void testGetAuditsWithUnAuthorizedUserExpectAccessDenial() throws Exception {
        ResultActions result = mockMvc
        		.perform(get("/api/log/auditLog/?fromDateAsStr=&toDateAsStr=&revision=&userId=&revisionType=&funcName=&tableName=&businessKey=&page=&size=&sort=")
        		.with(httpBasic("dumyUser","password1")));

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

	@Test
    public void testGetAuditFuncNamesWithAuthorizedUserExpectSuccess() throws Exception {

        // when
        ResultActions result = mockMvc.perform(get("/api/log/funcNames?funcName=")
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        // then
        result.andDo(print())
                .andExpect(status().isOk());
    }
	
	@Test
    public void testGetAuditFuncNamesWithUnAuthorizedUserExpectAccessDenial() throws Exception {

        ResultActions result = mockMvc.perform(get("/api/log/funcNames?funcName=")
        		.with(httpBasic("dumyUser","password1")));

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

	@Test
	public void testSearchViolationLogWithAuthorizedUserExpectSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/log/violationAccess?fromDateAsStr=&toDateAsStr=&loginName=appadmin")
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testSearchViolationLogWithAuthorizedUserExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/log/violationAccess?fromDateAsStr=&toDateAsStr=&loginName=appadmin")
        		.with(httpBasic("dumyUser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	
	@Test
    public void testGetDownloadFileWithAuthorizedUserExpectSuccess() throws Exception {

        // when
		when(auditLogService.getDownloadFile(any())).thenReturn(new DownloadFile());
		
        ResultActions result = mockMvc
        		.perform(get("/api/log/downloadFile?fromDateAsStr=&toDateAsStr=&revision=1&userId=&revisionType=&funcName=&tableName=TBL_AA_SUBJECT&businessKey=")
        		.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
			
		result.andDo(print());

    }
	
	@Test
    public void testGetDownloadFileWithUnAuthorizedUserExpectAccessDenial() throws Exception {

        ResultActions result = mockMvc
        		.perform(get("/api/log/downloadFile?fromDateAsStr=&toDateAsStr=&revision=1&userId=&revisionType=&funcName=&tableName=&businessKey=")
        		.with(httpBasic("dumyUser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	
	@Test
    public void testGetexportWithUnAuthorizedUserExpectAccessDenial() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/api/log/exportCSV?fromDateAsStr=&toDateAsStr=&revision=&userId=&revisionType=&funcName=&tableName=&businessKey=")
        		.with(httpBasic("dumyUser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
    public void testGetexportWithAuthorizedUserExpectSuccess() throws Exception {
		when(auditLogService.exportToCSV(any())).thenReturn(null);
		
		ResultActions result = mockMvc
				.perform(get("/api/log/exportCSV?fromDateAsStr=&toDateAsStr=&revision=&userId=&revisionType=&funcName=&tableName=&businessKey=")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
			
		result.andDo(print());
	}
}
