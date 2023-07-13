package com.ncs.iconnect.sample.lab.generated.permission;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.transaction.Transactional;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;

import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.itrust5.ss5.domain.Subject;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class RoleAdminIT {
	
	@Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private RoleService roleService;
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
	
	@Test
	public void testCreateRoleWithAuthorizedAndExceptAccessSuccess() throws Exception{
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceName("DEF-role-testrole");
		
		ResultActions result = mockMvc.perform(get("/api/roles")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(res))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		 result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testCreateRoleWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceName("DEF-role-testrole");
		
		ResultActions result = mockMvc.perform(get("/api/roles")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(res))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testupdateRoleAuthorizedAndExceptAccessSuccess() throws Exception{
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("LOG_ROLE");
		entity.setResourceId("DEF-role-app-createTest");
		entity.setResourceDesc("DEF-role-app-createTest");
		entity.setResourceName("test uri");
		entity.setResourcePath("");
		entity.setRes2RessForResourceId(null);
		entity.setGroup2Ress(null);
		entity.setRes2ResTOsForParentResId(null);
		entity.setSubject2Ress(null);
		this.roleService.save(entity);
		
		entity.setResourceDesc("updated createTest role");
		
		ResultActions result = mockMvc.perform(put("/api/roles")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(entity))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testupdateRoleUnAuthorizedAndExceptAccessDenial() throws Exception{
		Resource entity = new Resource();
		entity.setAppId("IConnect");
		entity.setResourceType("LOG_ROLE");
		entity.setResourceId("DEF-role-app-createTest");
		entity.setResourceDesc("DEF-role-app-createTest");
		entity.setResourceName("test uri");
		entity.setResourcePath("");
		entity.setRes2RessForResourceId(null);
		entity.setGroup2Ress(null);
		entity.setRes2ResTOsForParentResId(null);
		entity.setSubject2Ress(null);
		this.roleService.save(entity);
		
		entity.setResourceDesc("updated createTest role");
		
		ResultActions result = mockMvc.perform(put("/api/roles")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(entity))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testFindAllApplicationAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/applications")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("roleadmin", "DEF-user-roleadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testFindAllApplicationUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/applications")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindRolesWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles?applicationId=IConnect&roleName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("roleadmin", "DEF-user-roleadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindRolesWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles?applicationId=IConnect&roleName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyUser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetRoleWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/DEF-role-appadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("roleadmin", "DEF-user-roleadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testGetRoleWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/DEF-role-appadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	private Resource setCascadeObjectNull(Resource res) {
			res.setGroup2Ress(null);
			res.setRes2RessForResourceId(null);
			res.setRes2ResTOsForParentResId(null);
			res.setSubject2Ress(null);
			return res;
	}
	 
	@Test
	@Transactional
	public void testDeleteRoleWithAuthorizedAndExceptAccessSuccess() throws Exception{
		
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-role-testRole");
		res.setResourceName("DEF-role-testRole");
		res.setResourceType("LOG_ROLE");
		setCascadeObjectNull(res);
		this.roleService.save(res);
		em.detach(res);
		
		ResultActions result = mockMvc.perform(delete("/api/roles/DEF-role-testRole")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteRoleWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		Resource res = new Resource();
		res.setAppId("IConnect");
		res.setResourceId("DEF-role-testRole2");
		res.setResourceName("DEF-role-testRole2");
		res.setResourceType("LOG_ROLE");
		setCascadeObjectNull(res);
		final Resource savedRes = this.roleService.save(res);
		
		ResultActions result = mockMvc.perform(delete("/api/roles/DEF-role-testRole2")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized())
		.andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult result) throws Exception {
				roleService.delete(savedRes);
			}
		});
	}
	
	@Test
	public void testfindAllAssignedUsersWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/assignedUsers/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindAllAssignedUsersWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/assignedUsers/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindAllAssignedFuncsWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/assignedFuncs/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindAllAssignedFuncsWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/assignedFuncs/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindAllAssignedGroupsWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/assignedGroups/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindAllAssignedGroupsWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/roles/assignedGroups/DEF-role-configadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testUnassignedUsersWithAuthorizedAndExceptAccessSuccess() throws Exception{
		Subject[] subjects = new Subject[1];
		Subject subj = new Subject();
		subj.setSubjectId("DEF-user-useradmin");
		subjects[0] = subj;
		
		ResultActions result = mockMvc
				.perform(put("/api/roles/unAssignUsers/DEF-role-useradmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(subjects))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(put("/api/roles/assignUsers/DEF-role-useradmin")
			        		.contentType(MediaType.APPLICATION_JSON)
			        		.content(TestUtil.convertObjectToJsonBytes(subjects))
			        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
				}
			});
	}
	
	@Test
	public void testUnassignedUsersWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		Subject[] subjects = new Subject[1];
		Subject subj = new Subject();
		subj.setSubjectId("DEF-user-appadmin");
		subjects[0] = subj;
		
		ResultActions result = mockMvc
				.perform(put("/api/roles/unAssignUsers/DEF-role-appadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(subjects))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testUnAssignedFuncsWithAuthorizedAndExceptAccessSuccess() throws Exception{
		
		Resource[] ress = new Resource[1];
		Resource resource = new Resource();
		resource.setAppId("IConnect");
		resource.setResourceId("DEF-uri-app-create");
		ress[0] = resource;
		
		ResultActions result = mockMvc
				.perform(put("/api/roles/unAssignFuncs/DEF-func-acm-application")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		result.andDo(print());
	}
	
	@Test
	public void testUnAssignedFuncsWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		Resource[] ress = new Resource[1];
		Resource resource = new Resource();
		resource.setAppId("IConnect");
		resource.setResourceId("DEF-uri-codeadmin-delTypes");
		ress[0] = resource;
		
		ResultActions result = mockMvc
				.perform(put("/api/roles/unAssignFuncs/DEF-func-maintain-codeadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(ress))
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindUnAssignedGroupsWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/roles/search/unAssignedGroups?resourceId=&groupName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnAssignedGroupsWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/roles/search/unAssignedGroups?resourceId=&groupName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	public void testfindUnassignedUsersWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/roles/search/unAssignedUsers?resourceId=&firstName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnassignedUsersWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/roles/search/unAssignedUsers?resourceId=&firstName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindUnAssignedFuncsWithAuthorizedAndExceptAccessSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/roles/search/unAssignedFuncs?resourceId=&roleName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testfindUnAssignedFuncsWithUnAuthorizedAndExceptAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/roles/search/unAssignedFuncs?resourceId=&roleName=&page=1&size=10&sort=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
}
