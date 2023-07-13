package com.ncs.iconnect.sample.lab.generated.permission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.boot.test.context.SpringBootTest;

import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.ss5.domain.Group;
import com.ncs.itrust5.ss5.domain.Group2Res;
import com.ncs.itrust5.ss5.domain.Group2ResId;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.itrust5.ss5.domain.Subject;
import com.ncs.itrust5.ss5.domain.Subject2Group;
import com.ncs.itrust5.ss5.domain.Subject2GroupId;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class GroupAdminIT {
	
	@Autowired
    private WebApplicationContext wac;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;
	private MockMvc mockMvc;
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
    public void testGetGroupAPIWithAuthorizedUserExpectSuccess() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/api/groups/?groupId=&groupName=&page=1&size=10&sort=")
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

        // then
        result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testGroupAdminCreateGroupWithAuthorizedExpectSuccess() throws Exception{
		Group gp = new Group();
		gp.setGroupId("DEF-group-TEST_GROUP");
		gp.setGroupName("TEST_GROUP");
		gp.setLeftIndex(13);
		gp.setRightIndex(14);
		gp.setCreatedBy("SYSTEM");
		
		ResultActions result = mockMvc
				 	.perform(post("/api/groups")
				 	.contentType(MediaType.APPLICATION_JSON)
				 	.content(TestUtil.convertObjectToJsonBytes(gp))
				 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));

	     result.andDo(print())
	     	.andExpect(status().isCreated())
	     	.andDo(new ResultHandler() {
	     		//restore the status
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(delete("/api/groups/DEF-group-TEST_GROUP")
						 	.contentType(MediaType.APPLICATION_JSON)
						 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
				}
	     	});
	}
	
	
	@Test
	public void testGroupAdminCreateGroupWithUnAuthorizedExpectAccessReject() throws Exception{
		Group gp = new Group();
		gp.setGroupId("DEF-group-TEST_GROUP");
		gp.setGroupName("TEST_GROUP");
		gp.setLeftIndex(13);
		gp.setRightIndex(14);
		gp.setCreatedBy("SYSTEM");
		
		ResultActions result = mockMvc
				 	.perform(post("/api/groups")
				 	.contentType(MediaType.APPLICATION_JSON)
				 	.content(TestUtil.convertObjectToJsonBytes(gp))
	        		.with(httpBasic("dumyuser","password1")));

	     result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testGroupAdminUpdateGroupWithUnAuthorizedExpectSuccess() throws Exception{
		Group gp = null;
		gp = this.groupService.find("REQ-group-extranet");
		assertThat(gp).isNotNull();

		gp.setGroup2Ress(null);
		gp.setSubject2Groups(null);
		
		mockMvc.perform(get("/api/groups?groupId=REQ-group-extranet&groupName=GROUP A&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(gp))
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));

		result.andDo(print()).andExpect(status().isOk());
		
	}
	
	@Test
	@Transactional
	public void testGroupAdminUpdateGroupWithUnAuthorizedExpectAccessDenial() throws Exception{
	
		Group gp = null;
		gp = this.groupService.find("REQ-group-universal");
		assertThat(gp).isNotNull();
		
		Set<Group2Res> grp2resSet = new HashSet<Group2Res>();
		Group2Res grp2res = new Group2Res();
		grp2res.setId(new Group2ResId("REQ-group-universal","DEF-role-appadmin"));
		grp2resSet.add(grp2res);
		
		gp.setGroup2Ress(grp2resSet);
		
		Set<Subject2Group> subj2Gps = new HashSet<Subject2Group>();
		Subject2Group subj2Gp = new Subject2Group();
		subj2Gp.setId(new Subject2GroupId("DEF-user-configadmin","REQ-group-universal"));
		subj2Gps.add(subj2Gp);
		gp.setSubject2Groups(subj2Gps);
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(gp))
        		.with(httpBasic("dumyuser","password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void testFindGroupsWithAuthorizedExpectSuccess() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api/groups?groupId=DEF-group-groupA&groupName=GROUP A&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));

		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testFindGroupsWithUnAuthorizedExpectAccessDenial() throws Exception{
		
		ResultActions result = mockMvc
			 	.perform(get("/api/groups?groupId=DEF-group-groupA&groupName=GROUP A&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetGroupsWithAuthorizedExpectSuccess() throws Exception{
		
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/DEF-group-organization")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));

		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testGetGroupsWithUnAuthorizedExpectAccessDenial() throws Exception{
		
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/DEF-group-groupA")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	private Group prepareGroup(Group grp) {
		grp.setGroup2Ress(null);
		grp.setGroups(null);
		grp.setSubject2Groups(null);
		return grp;
	}
	
	@Test
	@Transactional
	public void testDeleteGroupsWithAuthorizedExpectSuccess() throws Exception{
		
		Group test = new Group();
		test.setGroupId("DEF-group-testA");
		prepareGroup(test);
		test.setGroupName("DEF-group-testGrp1");
		test.setGroupParentId("REQ-group-universal");
		this.groupService.save(test);
		
		ResultActions result = mockMvc
			 	.perform(delete("/api/groups/DEF-group-testA")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteGroupsWithAuthorizedExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
			 	.perform(delete("/api/groups/DEF-group-groupA")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testFindAllAssignedUsersWithSuccess() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/assignedUsers/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testFindAllAssignedUsersWithAccessDenial() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/assignedUsers/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindAllAssignedRolesWithSuccess() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/assignedRoles/REQ-group-extranet")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindAllAssignedRolesWithAccessDenial() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api//groups/assignedRoles/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testunAssignUsersWithSuccess() throws Exception{
		Subject subj[] = new Subject[1];
		Subject sub = new Subject();
		sub.setSubjectId("DEF-user-useradmin");
		subj[0] = sub;
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/unAssignUsers/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(subj))
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testunAssignUsersWithAccessDenial() throws Exception{
		Subject subj[] = new Subject[1];
		Subject sub = new Subject();
		sub.setSubjectId("DEF-user-useradmin");
		subj[0] = sub;
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/unAssignUsers/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(subj))
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testunAssignRolesWithAccessSuccess() throws Exception{
		Resource[] resources = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("REQ-group-universal");
		
		resources[0] = res;
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/unAssignRoles/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(resources))
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		result.andDo(print());
	}
	
	@Test
	public void testunAssignRolesWithAccessDenial() throws Exception{
		Resource[] resources = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("REQ-group-universal");
		
		resources[0] = res;
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/unAssignRoles/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(resources))
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testfindUnassignedUsersAccessSuccess() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/search/unAssignedUsers?groupId=REQ-group-universal&firstName&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnassignedUsersAccessDenial() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api/groups/search/unAssignedUsers?groupId=REQ-group-universal&firstName&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testAssignUsersWithAccessSuccess() throws Exception{
		Subject subj[] = new Subject[1];
		Subject sub = new Subject();
		sub.setSubjectId("DEF-user-useradmin");
		subj[0] = sub;
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/assignUsers/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(subj))
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testAssignUsersWithAccessDenial() throws Exception{
		Subject subj[] = new Subject[1];
		Subject sub = new Subject();
		sub.setSubjectId("DEF-user-useradmin");
		subj[0] = sub;
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/assignUsers/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(subj))
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	public void testfindUnassignedRolesWithAccessSuccess() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api//groups/search/unAssignedRoles?groupId=REQ-group-universa&roleName=Common Role&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testfindUnassignedRolesWithAccessDenial() throws Exception{
		ResultActions result = mockMvc
			 	.perform(get("/api//groups/search/unAssignedRoles?groupId=REQ-group-universa&roleName=Common Role&page=1&size=10&sort=")
			 	.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testAssignRolesWithAccessSuccess() throws Exception{
		
		Resource[] resources = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("DEF-role-configadmin");
		
		resources[0] = res;
		
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/assignRoles/REQ-group-universal")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(resources))
			 	.header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(put("/api/groups/unAssignRoles/REQ-group-universal")
						 	.contentType(MediaType.APPLICATION_JSON)
						 	.content(TestUtil.convertObjectToJsonBytes(resources))
			 	            .header("Authorization","Bearer " + obtainAccessToken("groupadmin", "DEF-user-groupadmin", "password1")));
				}
			});
	}
	
	@Test
	public void testAssignRolesWithAccessDenial() throws Exception{
		Resource[] resources = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("DEF-role-groupadmin");
		
		resources[0] = res;
		ResultActions result = mockMvc
			 	.perform(put("/api/groups/assignRoles/DEF-group-groupA")
			 	.contentType(MediaType.APPLICATION_JSON)
			 	.content(TestUtil.convertObjectToJsonBytes(resources))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
}
