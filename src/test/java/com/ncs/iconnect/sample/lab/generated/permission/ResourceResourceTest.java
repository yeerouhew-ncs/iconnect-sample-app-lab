package com.ncs.iconnect.sample.lab.generated.permission;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import org.springframework.http.MediaType;
import javax.persistence.EntityManager;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.acm.function.service.ResourceService;
import com.ncs.itrust5.ss5.domain.Application;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ResourceResourceTest  {

	@Autowired
	private ResourceService resourceService;
	@Autowired
	private AcmApplicationService acmAppService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private Filter springSecurityFilterChain;
	private MockMvc mockMvc;
	private static final String APP_ID = "IConnect";
	@Autowired
	private EntityManager em;
	@Autowired
    private TestTokenProvider testTokenProvider;
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
	
	@BeforeEach
	public void initial() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
	}

	private Timestamp getTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	private Resource setCascadeObjectNull(Resource res) {
		res.setGroup2Ress(null);
		res.setRes2RessForResourceId(null);
		res.setRes2ResTOsForParentResId(null);
		res.setSubject2Ress(null);
		return res;
	}
	
	private Resource createResourceEntity() {
		Resource res = new Resource();
		res.setResourceId("DEF-uri-test-get");
		res.setResourceId("DEF-func-acm-test");
		res.setAppId("IConnect");
		res.setResourceName("FUNCTION-TEST");
		res.setResourceType("URI");
		res.setCreatedBy("ADMIN");
		res.setCreatedDt(getTimestamp());
		res.setUpdatedBy("ADMIN");
		res.setUpdatedDt(getTimestamp());
		
		res = setCascadeObjectNull(res);
		return res;
	}
	
	@Test
	@Transactional
	public void testCreateResourceWithAuthorizedExpectSuccess() throws Exception {
		
		Resource res = createResourceEntity();
		
		ResultActions result = mockMvc
				.perform(post("/api/resources")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		result.andDo(print()).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	public void testCreateResourceWithUnAuthorizedExpectAccessDenial() throws Exception {
		
		Resource res = createResourceEntity();
		
		ResultActions result = mockMvc
				.perform(post("/api/resources")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res))
				.with(httpBasic("dumyuser", "password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testupdateResourceWithAuthorizedExpectSuccess() throws Exception {
		
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("URI");
		entity.setResourceId("DEF-uri-app-createTestURI");
		entity.setResourceDesc("DEF-uri-app-createTestURI");
		entity.setResourceName("test uri");
		entity.setResourcePath("/api/applications/createTestURI[GET]");
		this.initialCascadedResource(entity);
		this.resourceService.save(entity);
		
		Resource testRes = resourceService.find("DEF-uri-app-createTestURI");
		
		Application app = new Application();
		app.setAppCode("IConnect");
		app.setAppId("IConnect");
		app.setAppName("IConnect");
		testRes.setApplication(app);
		
		ResultActions result = mockMvc
				.perform(put("/api/resources")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(testRes))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());	
	}
	
	
	@Test
	public void testFindAllApplicationWithAuthorizedExpectSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/resources/applications")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testFindAllApplicationWithUnAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/resources/applications")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindResourcesWithAuthorizedExpectSuccess() throws Exception{
		String url = "/api/resources?";
		String[] parameter = {"IConnect","FUNCTION",""};
		
		String urlContact = "page=0&size=15&sort=asc"+"&applicationId=" + parameter[0] 
				+ "&resourceType=" + parameter[1]
				+ "&resourceName=" + parameter[2]
				+ "&resourcePath=" + null;
		
		ResultActions result = mockMvc
				.perform(get(url+urlContact)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void testfindResourcesWithUnAuthorizedExpectAccessDenial() throws Exception{
		String url = "/api/resources?";
		String[] parameter = {"IConnect","FUNCTION",""};
		
		String urlContact = "page=0&size=15&sort=asc"+"&applicationId=" + parameter[0] 
				+ "&resourceType=" + parameter[1]
				+ "&resourceName=" + parameter[2]
				+ "&resourcePath=" + null;
		
		ResultActions result = mockMvc
				.perform(get(url+urlContact)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print())
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetResourceByResourceIdWithAuthorizedExpectSuccess() throws Exception {
		String resourceId = "DEF-func-acm-application";
		String getURL = "/api/resources/"+resourceId;
		
		ResultActions result = mockMvc
				.perform(get(getURL)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.resourceId").value("DEF-func-acm-application"));
	}
	
	@Test
	public void testGetResourceByResourceIdWithUnAuthorizedExpectAccessDenial() throws Exception {
		String resourceId = "DEF-func-acm-application";
		String getURL = "/api/resources/"+resourceId;
		
		ResultActions result = mockMvc
				.perform(get(getURL)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testDeleteResourceWithAuthorizedExpectSuccess() throws Exception{
		
		Resource res = new Resource();
		res.setResourceId("DEF-uri-resource-delete");
		res.setAppId("IConnect");
		res.setResourceName("Param_Get");
		res.setResourceDesc("Get paramter");
		res.setResourcePath("/paramadmin**/**[GET]");
		res.setResourceType("URI");
		this.initialCascadedResource(res);
		this.resourceService.save(res);
		this.em.detach(res);
		
		ResultActions result = mockMvc
				.perform(delete("/api/resources/DEF-uri-resource-delete")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteResourceWithUnAuthorizedExpectAccessDenial() throws Exception{
		
		ResultActions result = mockMvc
				.perform(delete("/api/resources/DEF-uri-resource-delete")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testunAssignFunctionsAuthorizedExpectSuccess() throws Exception{
		
		Resource res[] = new Resource[1];
		Resource resource = new Resource();
		resource.setResourceId("DEF-func-acm-application");
		res[0] = resource;
		
		//first delete the res2res
		mockMvc.perform(put("/api/resources/unAssignFunctions/DEF-uri-app-getById")
		.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
		.content(TestUtil.convertObjectToJsonBytes(res))
		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		//restore the DB mapping status
		ResultActions result = mockMvc.perform(put("/api/resources/assignFunctions/DEF-uri-app-getById")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void testunAssignFunctionsUnAuthorizedExpectAccessDenial() throws Exception{
		Resource res[] = new Resource[1];
		Resource resource = new Resource();
		resource.setResourceId("DEF-func-acm-application");
		
		res[0] = resource;
		
		ResultActions result = mockMvc
				.perform(put("/api/resources/unAssignFunctions/DEF-uri-app-get")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res))
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindUnAssignFunctionsWithAuthorizeExpectAccessSuccess() throws Exception{
		
		ResultActions result = mockMvc
				.perform(get("/api/resources/search/unAssignFunctions?applicationId=IConnect&resourceId=&functionName=&page=1&size=10&sort=")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnAssignFunctionsWithUnAuthorizeExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/resources/search/unAssignFunctions?applicationId=IConnect&resourceId=&functionName=&page=1&size=10&sort=")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	private void initialCascadedResource(Resource entity) {
		entity.setRes2RessForResourceId(null);
		entity.setGroup2Ress(null);
		entity.setRes2ResTOsForParentResId(null);
		entity.setSubject2Ress(null);
	}
	
	@Test
	@Transactional
	public void testAssignFunctionsAuthorizedExpectSuccess() throws Exception{
		
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("URI");
		entity.setResourceId("DEF-uri-app-createTest");
		entity.setResourceDesc("DEF-uri-app-createTest");
		entity.setResourceName("test uri");
		entity.setResourcePath("/api/applications[GET]");
		this.initialCascadedResource(entity);
		this.resourceService.save(entity);
		this.em.detach(entity);
		
		Resource res[] = new Resource[1];
		Resource resource = new Resource();
		resource.setResourceId("DEF-func-acm-application");
		
		res[0] = resource;
		
		ResultActions result = mockMvc
				.perform(put("/api/resources/assignFunctions/DEF-uri-app-createTest")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk())
			.andDo(new ResultHandler() {
				//restore the status
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(put("/api/resources/unAssignFunctions/DEF-uri-app-createTest")
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(res))
						.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
					
					Resource existing = resourceService.find("DEF-uri-app-createTest");
					resourceService.delete(existing);
				}
			});
	}
	
	@Test
	public void testAssignFunctionsUnAuthorizedExpectAccessDenial() throws Exception{
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("URI");
		entity.setResourceId("DEF-uri-app-createTest");
		entity.setResourceDesc("DEF-uri-app-createTest");
		entity.setResourceName("test uri");
		entity.setResourcePath("/api/applications[GET]");
		this.initialCascadedResource(entity);
		this.resourceService.save(entity);
		
		Resource res[] = new Resource[1];
		Resource resource = new Resource();
		resource.setResourceId("DEF-func-acm-application");
		res[0] = resource;
		
		ResultActions result = mockMvc
				.perform(put("/api/resources/assignFunctions/DEF-fun-app-createTest")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res))
				.with(httpBasic("dumyuser", "password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
}
