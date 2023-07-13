package com.ncs.iconnect.sample.lab.generated.permission;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import org.springframework.http.MediaType;
import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.boot.test.context.SpringBootTest;


import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.user.repository.AcmUserRepository;
import com.ncs.itrust5.acm.user.service.UserService;
import com.ncs.itrust5.ss5.domain.Group;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.itrust5.ss5.domain.Subject;
import com.ncs.itrust5.ss5.domain.SubjectLogin;

import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iconnect.sample.lab.generated.security.jwt.JWTFilter;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class UserAdminIT {

	@Autowired
    private WebApplicationContext wac;
	@Autowired
	private Filter springSecurityFilterChain;
	private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
	private GroupService groupService;
    @Autowired
    private AcmUserRepository userRepository;
    @Autowired
	private EntityManager em;
    @Autowired
    TestTokenProvider testTokenProvider;
    
	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
		return testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
	}
	
	private String getFakeAccessToken() {
		return "ABCDE";
	}
	
	private Subject getCreateSubject(String loginName,String subjectId) {
		Subject subject = new Subject();
		subject.setSubjectId("DEF-user-dumyuser");
		subject.setFirstName(loginName);
		subject.setLastName(loginName);
		subject.setEmail(loginName+"@gmail.com");
		subject.setStatus("A");
		subject.setLogicalDel(0);
		subject.setCreatedBy("SYSTEM");
		subject.setCreatedDt(new Timestamp(System.currentTimeMillis()));
		
		Set<SubjectLogin> sublogin = new HashSet<SubjectLogin>();
		SubjectLogin login = new SubjectLogin();
		login.setSubjectId("DEF-user-dumyuser");
		login.setLoginMechanism("PASSWORD");
		login.setLoginName(loginName);
		login.setPasswordChangedDate(new Timestamp(System.currentTimeMillis()));
		sublogin.add(login);
		subject.setSubjectLogins(sublogin);
		return subject;
	}
	
	@Test
	@Transactional
	public void testUserAdminCreateUserWithAuthorizedExpectSuccess() throws Exception{
		Subject subject = getCreateSubject("dumyuser","DEF-user-dumyuser");
		
		ResultActions result = mockMvc.perform(post("/api/users")
				.content(TestUtil.convertObjectToJsonBytes(subject))
				.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.lastName").value("dumyuser"))
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					SubjectLogin exist = userService.getSubjectLoginByUsernameAndLoginMethod("dumyuser", "PASSWORD");
					userRepository.deleteById(exist.getSubjectId());
				}
			});
	}
	
	@Test
	@Transactional
	public void testUserAdminCreateUserWithUnAuthorizedExpectAccessDenial() throws Exception{
		Subject subject = getCreateSubject("dumyuser4","DEF-user-dumyuser4");
		ResultActions result = mockMvc.perform(post("/api/users")
				.content(TestUtil.convertObjectToJsonBytes(subject))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print())
				.andExpect(status().isUnauthorized());
	}
	
	@Transactional(readOnly = true)
	public Subject findSubjectAndCascadedSubjectLogin() {
		Subject sujb = userService.find("DEF-user-useradmin");
		return sujb;
	}
	
	@Test
	@Transactional
	public void testAdminUpdateUserWithAuthorizedExpectedSuccess() throws Exception{
		Subject subj = findSubjectAndCascadedSubjectLogin();
		subj.setPhoneNum(subj.getPhoneNum()+1);
		userService.prepareSubject(subj);
		this.em.flush();
		
		ResultActions results = mockMvc.perform(put("/api/users")
				.content(TestUtil.convertObjectToJsonBytes(subj))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		results.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testUserAdminUpdateUserWithUnAuthorizedExpectedAccessDenial() throws Exception{
		Subject subj = findSubjectAndCascadedSubjectLogin();
		subj.setPhoneNum("123456789");
		userService.prepareSubject(subj);
		em.flush();
		
		ResultActions result = mockMvc.perform(put("/api/users")
				.content(TestUtil.convertObjectToJsonBytes(subj))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization",getFakeAccessToken()));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	
	@Test
	@Transactional
    public void testFindUserAPIWithAuthorizedUserExpectSuccess() throws Exception {

        ResultActions result = mockMvc.perform(get("/api/users/?effectiveDt=&email=&expiryDt=&firstName=&lastName=&loginMechanism=&loginName=&page=1&size=10&sort=&status=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1"))
        		);
        result.andDo(print()).andExpect(status().isOk());

        result = mockMvc.perform(get("/api/users/?effectiveDt=&email=&expiryDt=&firstName=&lastName=&loginMechanism=&loginName=&page=1&size=10&sort=&status=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
        result.andDo(print()).andExpect(status().isOk());
	}

	@Test
    public void testFindUserAPIWithUnauthorizedUserExpectException() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/users/?effectiveDt=&email=&expiryDt=&firstName=&lastName=&loginMechanism=&loginName=&page=1&size=10&sort=&status=")
        		.contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("dumyuser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testGetUserIWithAuthorizedUserExpectSuccess() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/users/DEF-user-configadmin")
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value("configadmin@ncs.com.sg"));
	}
	
	
	@Test
	@Transactional
	public void testGetUserIWithUnAuthorizedUserExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/users/DEF-user-configadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testFindAllAssignedRolesWithAuthorizedUserExpectSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/assignedRoles/DEF-user-configadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testFindAllAssignedRolesWithUnAuthorizedUserExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/assignedRoles/DEF-user-configadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testfindAllAssignedGroupsWithAuthorizedUserExpectSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/assignedGroups/DEF-user-configadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testFindAllAssignedGroupssWithUnAuthorizedUserExpectAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/assignedGroups/DEF-user-configadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testUnAssignUnRolesAuthorizedUserExpectAccessDenial() throws Exception{
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("DEF-role-configadmin");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/unAssignRoles/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(ress))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testUnAssignGroupsSuccess() throws Exception{
		Group groups[] = new Group[1];
		Group gp = new Group();
		gp.setGroupId("REQ-group-universal");
		groups[0] = gp;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/unAssignGroups/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(groups))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc
					.perform(put("/api/users/assignGroups/DEF-user-configadmin")
					.content(TestUtil.convertObjectToJsonBytes(groups))
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
				}
			});
	}
	
	@Test
	@Transactional
	public void testUnAssignGroupsAccessDenial() throws Exception{
		Group groups[] = new Group[1];
		Group gp = new Group();
		gp.setGroupId("REQ-group-universal");
		groups[0] = gp;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/unAssignGroups/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(groups))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testAssignRolesAuthorizedUserExpectSuccess() throws Exception{
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("DEF-role-groupadmin");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/assignRoles/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(ress))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					//restore the status
					Resource ress[] = new Resource[1];
					Resource res = new Resource();
					res.setResourceId("DEF-role-groupadmin");
					ress[0] = res;
					
					mockMvc.perform(put("/api/users/unAssignRoles/DEF-user-configadmin")
							.content(TestUtil.convertObjectToJsonBytes(ress))
							.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
				}
			});
	
	}
	
	@Test
	@Transactional
	public void testAssignRolesUnAuthorizedUserExpectAccessDenial() throws Exception{
		Resource ress[] = new Resource[1];
		Resource res = new Resource();
		res.setResourceId("DEF-role-groupadmin");
		ress[0] = res;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/assignRoles/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(ress))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	private Group prepateGroup(Group grp) {
		grp.setGroup2Ress(null);
		grp.setGroups(null);
		grp.setSubject2Groups(null);
		return grp;
	}
	    
	@Test
	@Transactional
	public void testAssignGroupsSuccess() throws Exception {
		Group test = new Group();
		test.setGroupId("DEF-group-testGrp1");
		prepateGroup(test);
		test.setGroupName("DEF-group-testGrp1");
		test.setGroupParentId("REQ-group-universal");
		this.groupService.save(test);
		
		Group groups[] = new Group[1];
		Group gp = new Group();
		gp.setGroupId("DEF-group-testGrp1");
		groups[0] = gp;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/assignGroups/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(groups))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
	
				@Override
				public void handle(MvcResult result) throws Exception {
					Group groups[] = new Group[1];
					Group gp = new Group();
					gp.setGroupId("DEF-group-testGrp1");
					groups[0] = gp;
					
					mockMvc.perform(put("/api/users/unAssignGroups/DEF-user-configadmin")
							.content(TestUtil.convertObjectToJsonBytes(groups))
							.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
					
					Group existingGrp = groupService.find("DEF-group-testGrp1");
					prepateGroup(existingGrp);
					groupService.delete(existingGrp);
				}
				
			});
	}
	
	@Test
	@Transactional
	public void testAssignGroupsAccessDenial() throws Exception{
		Group groups[] = new Group[1];
		Group gp = new Group();
		gp.setGroupId("DEF-group-groupA");
		groups[0] = gp;
		
		ResultActions result = mockMvc
				.perform(put("/api/users/assignGroups/DEF-user-configadmin")
				.content(TestUtil.convertObjectToJsonBytes(groups))
				.contentType(MediaType.APPLICATION_JSON)
		        .with(httpBasic("dumyuser","password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testfindUnAssignedRolesSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/search/unAssignedRoles?subjectId=DEF-user-configadmin&roleName=appadmin&page=1&size=10&sort=&status=")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testfindUnAssignedRolesAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/search/unAssignedRoles?subjectId=DEF-user-configadmin&roleName=appadmin&page=1&size=10&sort=&status=")
				.contentType(MediaType.APPLICATION_JSON)
				//.header("Authorization","Bearer " + obtainAccessToken("dumyuser", "DEF-user-dumyuser", "password1")));
				.with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testFindUnAssignedGroupsSuccess() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/search/unAssignedGroups?subjectId=DEF-user-configadmin&groupName=testGroup&page=1&size=10&sort=&status=")
				.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testFindUnAssignedGroupsAccessDenial() throws Exception{
		ResultActions result = mockMvc
				.perform(get("/api/users/search/unAssignedGroups?subjectId=DEF-user-configadmin&groupName=testGroup&page=1&size=10&sort=&status=")
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("dumyuser","password1")));

		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	@Transactional
	public void testResetPasswordAccessSuccess() throws Exception{
       
		Subject subject = getCreateSubject("dumyuser1","DEF-user-dumyuser1");
		this.userService.save(subject);
		this.em.flush();
		
        ResultActions result = mockMvc
			.perform(post("/api/users/resetPassword")
			.contentType(MediaType.APPLICATION_JSON)
			.content("dumyuser1")
			.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testResetPasswordAccessDenial() throws Exception{
		ResultActions result = mockMvc
			.perform(post("/api/users/resetPassword")
			.contentType(MediaType.APPLICATION_JSON)
			.content("useradmin")
		    .with(httpBasic("dumyuser","password1")));
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
}