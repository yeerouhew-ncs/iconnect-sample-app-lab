package com.ncs.iconnect.sample.lab.generated.permission;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.itrust5.acm.function.service.ResourceService;
import com.ncs.itrust5.ss5.domain.Resource;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class FunctionAdminIT{
	
	@Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private ResourceService functionService;
    @Autowired
	private ResourceService resourceService;
	private MockMvc mockMvc;
	@Autowired
	private EntityManager em;
	@Autowired
    private TestTokenProvider testTokenProvider;
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }

    @BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}
	
    private Resource setCascadeObjectNull(Resource res) {
		res.setGroup2Ress(null);
		res.setRes2RessForResourceId(null);
		res.setRes2ResTOsForParentResId(null);
		res.setSubject2Ress(null);
		return res;
	}
    
	@Test
	public void testCreateFunctionWithAuthorizedExpectAccessSuccess() throws Exception{
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-fun-testfunction");
		res.setResourceName("FUNCTION-test function");
		
		ResultActions result = mockMvc.perform(post("/api/functions")
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.content(TestUtil.convertObjectToJsonBytes(res))
	        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
	    result.andDo(print()).andExpect(status().isCreated());
	}
	
	@Test
	public void testCreateFunctionWithUnAuthorizedExpectAccessDenial() throws Exception{
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceName("FUNCTION-test function");
		
		ResultActions result = mockMvc.perform(post("/api/functions")
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.content(TestUtil.convertObjectToJsonBytes(res))
	        		.with(httpBasic("dumyuser","password1")));
		
	    result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testUpdateFunctionWithAuthorizedExpectAccessSuccess() throws Exception{
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("FUNCTION");
		entity.setResourceId("DEF-fun-app-createTest");
		entity.setResourceDesc("DEF-fun-app-createTest");
		entity.setResourceName("test uri");
		entity.setResourcePath("");
		entity.setRes2RessForResourceId(null);
		entity.setGroup2Ress(null);
		entity.setRes2ResTOsForParentResId(null);
		entity.setSubject2Ress(null);
		this.functionService.save(entity);
		
		ResultActions result = mockMvc.perform(put("/api/functions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(entity))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testUpdateFunctionWithUnAuthorizedExpectAccessDenial() throws Exception{
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("FUNCTION");
		entity.setResourceId("DEF-fun-app-createTest");
		entity.setResourceDesc("DEF-fun-app-createTest");
		entity.setResourceName("test uri");
		entity.setResourcePath("");
		entity.setRes2RessForResourceId(null);
		entity.setGroup2Ress(null);
		entity.setRes2ResTOsForParentResId(null);
		entity.setSubject2Ress(null);
		this.functionService.save(entity);
		
		ResultActions result = mockMvc.perform(put("/api/functions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(entity))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindAllApplicationWithAuthorizedExpectAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/functions/applications")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].appId").value(hasItem("IConnect")));
	}
	
	@Test
	public void testfindAllApplicationWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/functions/applications")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindFunctionWithAuthorizedExpectAccessSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions?applicationId=IConnect&functionName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindFunctionWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions?applicationId=IConnect&functionName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print())
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testGetFunctionWithAuthorizedExpectAccessSuccess() throws Exception{
		
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-test2");
		res.setResourceName("FUNCTION-test function2");
		res.setResourceType("FUNCTION");
		this.setCascadeObjectNull(res);
		this.resourceService.save(res);
		this.em.detach(res);
		
		ResultActions result = mockMvc
				.perform(get("/api/functions/DEF-func-app-test2")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.resourceId").value("DEF-func-app-test2"))
		.andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult result) throws Exception {
				mockMvc
				.perform(delete("/api/functions/DEF-func-app-test2")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
			}
		});
	}
	
	@Test
	public void testGetFunctionWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions/DEF-func-app-health-check")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testdeleteFunctionWithAuthorizedExpectAccessSuccess() throws Exception{
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-test3");
		res.setResourceName("FUNCTION-testfunction3");
		res.setResourceType("FUNCTION");
		this.setCascadeObjectNull(res);
		this.resourceService.save(res);
		this.em.detach(res);
		
		ResultActions result = mockMvc
				.perform(delete("/api/functions/DEF-func-app-test3")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void testdeleteFunctionWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(delete("/api/functions/DEF-func-app-health-check")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testunAssignResourcesWithUnAuthorizedExpectAccessDenial() throws Exception{
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-metrics");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/unAssignResources/DEF-role-appadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindUnAssignResourcesWithAuthorizedExpectAccessSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions/search/unAssignResources?applicationId=IConnect&resourceId=&resourceName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnAssignResourcesWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions/search/unAssignResources?applicationId=IConnect&resourceId=&resourceName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testassignResourcesWithAuthorizedExpectAccessSuccess() throws Exception{
		
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-metrics");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/assignResources/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc
					.perform(put("/api/functions/unAssignResources/DEF-role-configadmin")
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.content(TestUtil.convertObjectToJsonBytes(ress))
	        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
				}
			});
	}
	
	@Test
	public void testassignResourcesWithUnAuthorizedExpectAccessDenial() throws Exception{
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-metrics");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/assignResources/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testUnassignResourcesWithAuthorizedExpectAccessSuccess() throws Exception{
		
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-metrics");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/assignResources/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testUnassignResourcesWithUnAuthorizedExpectAccessDenial() throws Exception{
		
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-metrics");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/assignResources/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindUnAssignRolesWithAuthorizedExpectAccessSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions/search/unAssignRoles?applicationId=IConnect&resourceId=&roleName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnAssignRolesWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/functions/search/unAssignResources?applicationId=IConnect&resourceId=&roleName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testassignRolesWithAuthorizedExpectAccessSuccess() throws Exception{
		
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-func-app-test3");
		res.setResourceName("FUNCTION-test function3");
		res.setResourceType("FUNCTION");
		this.setCascadeObjectNull(res);
		this.resourceService.save(res);
		em.detach(res);
		
		Resource ress[] = new Resource[1];
		Resource res2 = new Resource();
		res2.setAppId("IConnect");
		res2.setResourceId("DEF-role-configadmin");
		ress[0] = res2;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/assignResources/DEF-func-app-test3")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk())
		.andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult result) throws Exception {
				mockMvc
				.perform(put("/api/functions/unAssignResources/DEF-func-app-metrics")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
				
				Resource res = new Resource();
				res.setAppId("IConnect");
				res.setResourceId("DEF-func-app-test3");
				resourceService.delete(res);
			}
		});
	}
	
	@Test
	public void testassignRolesWithUnAuthorizedExpectAccessDenial() throws Exception{
		
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-role-configadmin");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/functions/assignResources/DEF-func-app-metrics")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
}
