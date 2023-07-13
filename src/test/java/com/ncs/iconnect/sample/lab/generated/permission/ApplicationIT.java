package com.ncs.iconnect.sample.lab.generated.permission;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.itrust5.acm.app.repository.ApplicationCrudRepository;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.ss5.domain.Application;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApplicationIT{

	@Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private AcmApplicationService amcApplicationService;
    @Mock
    private AcmApplicationService mockApplicationService;
    @Autowired
    private ApplicationCrudRepository repository;
    @Autowired
    private TestTokenProvider testTokenProvider;

	private MockMvc mockMvc;

	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
    
    @BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}
    
    @Test
    @Transactional
   	public void testCreateApplicationSuccess() throws Exception {
    	
   		Application application = createApp("testApp","SYSTEM");
   		application.setVersion(0);
   		
   		ResultActions result = mockMvc.perform(post("/api/applications")
   				.contentType(MediaType.APPLICATION_JSON)
   				.content(TestUtil.convertObjectToJsonBytes(application))
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
   		
   		result.andDo(print())
	   		.andExpect(status().isCreated())
	   		.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					deleteAppCode("testApp1");
				}
	   		});
   	}
    
    @Test
  	public void testCreateApplicationWithUnAuthorized() throws Exception {
    	
  		Application application = createApp("testApp2","SYSTEM");
  		application.setVersion(0);
  		
  		ResultActions result = mockMvc.perform(post("/api/applications")
  				.contentType(MediaType.APPLICATION_JSON)
  				.content(TestUtil.convertObjectToJsonBytes(application))
  				.with(httpBasic("dumyuser1","password1")));
  		
  		result.andDo(print())
	  		.andExpect(status().isUnauthorized())
	  		.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					deleteAppCode("testApp2");
				}
	  		});
  	}
    
    @Test
	public void testUpdateApplicationSuccess() throws Exception{
    	
		Application application = createApp("testAPP2","SYSTEM");
		Application saved = this.amcApplicationService.save(application);
		saved.setAppDesc("testAPP2 updated");
		
		ResultActions result = mockMvc.perform(put("/api/applications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(saved))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					deleteAppCode("testAPP2");
				}
			});
	}
	
	@Test
	public void testUpdateApplicationUnsuccess() throws Exception{
		Application application = createApp("testAPP3","SYSTEM");
		
		Application saved = this.amcApplicationService.save(application);
		saved.setAppDesc("testAPP3 updated");
		
		ResultActions result = mockMvc.perform(put("/api/applications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(saved))
				.with(httpBasic("dummyUser","password1")));
		
		result.andDo(print())
			.andExpect(status().isUnauthorized())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					deleteAppCode("testAPP3");
				}
	  		});
	}
	
    @Test
    public void testfindApplicationsAPIWithAuthorizedUserExpectSuccess() throws Exception {

        ResultActions result = mockMvc
        		.perform(get("/api/applications/?appCode='IConnect'&appDesc=&appName=&page=1&size=10&sort=")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        result.andDo(print()).andExpect(status().isOk());
	}
    
    @Test
    public void testfindApplicationsAPIWithUnAuthorizedUserExpectUnSuccess() throws Exception {
        ResultActions result = mockMvc
        		.perform(get("/api/applications/?appCode=&appDesc=&appName=&page=1&size=10&sort=")
        		.with(httpBasic("dumyuser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
	}
    
	@Test
    public void testGetApplicationSuccess() throws Exception {
        ResultActions result = mockMvc
        		.perform(get("/api/applications/IConnect")
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
    public void testGetApplicationUnAuthorized() throws Exception {
        ResultActions result = mockMvc
        		.perform(get("/api/applications/IConnect")
        		.with(httpBasic("dummyadmin","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
	}

	public Application createApp(String appCode,String createBy) {
		Application application = new Application();
		application.setAppId(appCode);
		application.setAppCode(appCode);
		application.setAppName(appCode);
		application.setAppDesc(appCode);
		application.setCreatedBy(createBy);
		return application;
	}
	
	@Transactional
	public void deleteAppCode(String appCode) {
		List<Application> appList = amcApplicationService.findByAppCode(appCode);
		appList.stream().forEach(app->{
			repository.delete(app);
		});
	}
	
	@Test
    public void testApplicationApplicationCodeExists() throws Exception {
		Application application = createApp("testAppExit","SYSTEM");
   		application.setVersion(0);
   		
   		ResultActions result = mockMvc.perform(post("/api/applications")
   				.contentType(MediaType.APPLICATION_JSON)
   				.content(TestUtil.convertObjectToJsonBytes(application))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
   		
   		List<Application> applicationList = new ArrayList<Application>();
   		applicationList.add(application);
   		when(mockApplicationService.findByAppCode(anySring())).thenReturn(applicationList);
   		
		assertSame(applicationList.size(), 1);

   		result = mockMvc.perform(post("/api/applications")
   				.contentType(MediaType.APPLICATION_JSON)
   				.content(TestUtil.convertObjectToJsonBytes(application))
   				.with(httpBasic("appadmin","password1")));
   		
   		result.andDo(print())
	   		.andExpect(status().is4xxClientError())
	   		.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					deleteAppCode("testAppExit");
				}
	  		});
	}

	private String anySring() {
		return null;
	}
}
