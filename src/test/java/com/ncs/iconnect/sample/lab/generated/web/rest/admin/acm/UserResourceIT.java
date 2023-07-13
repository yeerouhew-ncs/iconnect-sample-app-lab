package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import com.jayway.jsonpath.JsonPath;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm.UserResource;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.acm.user.repository.AcmUserRepository;
import com.ncs.itrust5.acm.user.service.UserService;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.domain.Group;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.itrust5.ss5.domain.Subject;
import com.ncs.itrust5.ss5.domain.SubjectLogin;
import com.ncs.itrust5.ss5.repository.SubjectLoginRepository;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class UserResourceIT {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private AcmUserRepository userRepostory;
	
	@Autowired
	private SubjectLoginRepository subjectLoginRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;
	private MockMvc restUserMockMvc;

	@Autowired
	private EntityManager em;

	@Autowired
	private OpenEntityManagerInViewFilter openEntityManagerInViewFilter;

	private Subject user;
	
	private static final String ROLE_ADMIN_USRE = "DEF-user-roleadmin";
	private static final String ROLE_ADMIN = "DEF-role-roleadmin";
	private static final String GROUP_ADMIN = "REQ-group-universal";
	private static final String USER_NAME = "roleadmin";
	
	

	public static Subject createEntity() {
		Subject user = new Subject();
		user.setFirstName("UserResource UT first name");
		user.setLastName("UserResource UT last name");
		user.setStatus(ITrustConstants.USER_STATUS_ACTIVE);
		return user;
	}

	@BeforeEach
	public void initTest() {
		user = createEntity();
	}

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final UserResource userRecource = new UserResource(userService, roleService, groupService);
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userRecource).addFilters(openEntityManagerInViewFilter)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService()).setMessageConverters(jacksonMessageConverter)
				.build();
	}

	@Test
	@Transactional
	public void createUser() throws Exception {
		Subject userTO = (Subject) BeanUtils.cloneBean(user);
		String responseString = restUserMockMvc
				.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(userTO)))
				.andExpect(status().isCreated()).andDo(print()).andReturn().getResponse().getContentAsString();
		String subjectId = JsonPath.parse(responseString).read("$.subjectId");
		Subject result = userRepostory.getOne(subjectId);
		assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
	}

	@Test
	@Transactional
	public void createUserWithSubjectLogin() throws Exception {
		Subject userTO = (Subject) BeanUtils.cloneBean(user);
		SubjectLogin subjectLogin = new SubjectLogin();
		subjectLogin.setLoginName("UserResource SubjectLogin test");
		subjectLogin.setLoginMechanism(ITrustConstants.AUTH_METHOD_PASS);
		Set<SubjectLogin> subjectLogins = new HashSet<SubjectLogin>() {
			{
				add(subjectLogin);
			}
		};
		userTO.setEmail("UT@ncsi.com.cn");
		userTO.setSubjectLogins(subjectLogins);
		String responseString = restUserMockMvc
				.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(userTO)))
				.andExpect(status().isCreated()).andDo(print()).andReturn().getResponse().getContentAsString();
		String subjectId = JsonPath.parse(responseString).read("$.subjectId");
		Subject result = userRepostory.getOne(subjectId);
		assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
	}

	@Test
	@Transactional
	public void updateUser() throws Exception {
		Subject userTO = (Subject) BeanUtils.cloneBean(user);
		SubjectLogin subjectLogin = new SubjectLogin();
		subjectLogin.setLoginName("UserResource SubjectLogin test");
		subjectLogin.setLoginMechanism(ITrustConstants.AUTH_METHOD_PASS);
		Set<SubjectLogin> subjectLogins = new HashSet<SubjectLogin>() {
			{
				add(subjectLogin);
			}
		};
		userTO.setEmail("UT@ncsi.com.cn");
		userTO.setSubjectLogins(subjectLogins);
		Subject subject = userRepostory.saveAndFlush(userTO);
		assertThat(subject.getSubjectId()).isNotBlank();
		Subject updateSubject = userRepostory.getOne(subject.getSubjectId());
		em.detach(updateSubject);
		updateSubject.setLastName("Updated Last Name");
		String responseString = restUserMockMvc
				.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(updateSubject)))
				.andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
		String subjectId = JsonPath.parse(responseString).read("$.subjectId");
		Subject result = userRepostory.getOne(subjectId);
		assertThat(result.getLastName()).isEqualTo("Updated Last Name");

	}

	@Test
	@Transactional
	public void findUsers() throws Exception {
		Subject userTO = (Subject) BeanUtils.cloneBean(user);
		userTO.setLastName("Test find Users Last Name");
		userRepostory.save(userTO);
		defaultUserShouldBeFound("lastName=" + userTO.getLastName(), userTO);
	}

	@Test
	@Transactional
	public void findUser() throws Exception {
		Subject userTO = (Subject) BeanUtils.cloneBean(user);
		Subject saveResult = userRepostory.save(userTO);
		assertThat(saveResult.getSubjectId()).isNotBlank();
		restUserMockMvc.perform(get("/api/users/{id}", saveResult.getSubjectId()))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.subjectId").value(equalTo(userTO.getSubjectId())))
				.andExpect(jsonPath("$.firstName").value(equalTo(userTO.getFirstName())))
				.andExpect(jsonPath("$.lastName").value(equalTo(userTO.getLastName())));
	}
	
	@Test
	@Transactional
	public void findAllAssignedRoles() throws Exception {
		restUserMockMvc.perform(get("/api/users/assignedRoles/{id}", ROLE_ADMIN_USRE))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].resourceId").value(hasItem(ROLE_ADMIN)));
	}
	
	@Test
	@Transactional
	public void findAllAssignedGroups() throws Exception {
		restUserMockMvc.perform(get("/api/users/assignedGroups/{id}", ROLE_ADMIN_USRE))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].groupId").value(hasItem(GROUP_ADMIN)));
	}
	
	@Test
	@Transactional
	public void assignAndUnAssignRoles() throws Exception {
		Resource rs = new Resource();
		rs.setResourceId(ROLE_ADMIN);
		Resource[] roles = new Resource[1];
		roles[0] = rs;
		restUserMockMvc.perform(put("/api/users/unAssignRoles/{subjectId}", ROLE_ADMIN_USRE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roles)))
				.andExpect(status().isOk());
		
		restUserMockMvc.perform(get("/api/users/assignedRoles/{id}", ROLE_ADMIN_USRE))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].resourceId").value(not(ROLE_ADMIN)));
		
		restUserMockMvc.perform(put("/api/users/assignRoles/{subjectId}", ROLE_ADMIN_USRE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roles)))
				.andExpect(status().isOk());
		
		restUserMockMvc.perform(get("/api/users/assignedRoles/{id}", ROLE_ADMIN_USRE))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].resourceId").value(hasItem(ROLE_ADMIN)));
	}
	
	@Test
	@Transactional
	public void findUnAssignedRoles() throws Exception {
		restUserMockMvc.perform(get("/api/users/search/unAssignedRoles?sort=resourceId,desc&subjectId="+ROLE_ADMIN_USRE+"&roleName="+ ROLE_ADMIN))
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	@Test
	@Transactional
	public void assignAndUnAssignGroups() throws Exception {
		Group grp = new Group();
		grp.setGroupId(GROUP_ADMIN);
		Group[] grps = new Group[1];
		grps[0] = grp;
		restUserMockMvc.perform(put("/api/users/unAssignGroups/{subjectId}", ROLE_ADMIN_USRE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grps)))
				.andExpect(status().isOk());
		
		restUserMockMvc.perform(get("/api/users/assignedGroups/{id}", ROLE_ADMIN_USRE))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].groupId").value(not(GROUP_ADMIN)));
		
		restUserMockMvc.perform(put("/api/users/assignGroups/{subjectId}", ROLE_ADMIN_USRE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grps)))
				.andExpect(status().isOk());
		
		restUserMockMvc.perform(get("/api/users/assignedGroups/{id}", ROLE_ADMIN_USRE))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].groupId").value(hasItem(GROUP_ADMIN)));
	}
	
	@Test
	@Transactional
	public void findUnAssignedGroups() throws Exception {
		restUserMockMvc.perform(get("/api/users/search/unAssignedGroups?sort=groupId,desc&subjectId="+ROLE_ADMIN_USRE+"&groupName="+ GROUP_ADMIN))
		.andExpect(status().isOk())
		.andDo(print());
	}	
	
	@Test
	@Transactional
	public void resetPassword() throws Exception, Exception {
		SubjectLogin login = subjectLoginRepository.findByLoginNameAndIdLoginMechanism(USER_NAME, ITrustConstants.AUTH_METHOD_PASS);
		em.detach(login);
		restUserMockMvc.perform(post("/api/users/resetPassword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(USER_NAME)))
				.andExpect(status().isOk())
				.andDo(print());
		SubjectLogin secondLogin = subjectLoginRepository.findByLoginNameAndIdLoginMechanism(USER_NAME, ITrustConstants.AUTH_METHOD_PASS);
		assertThat(login.getPassword()).isNotEqualTo(secondLogin.getPassword());
	}
	
	private void defaultUserShouldBeFound(String filter, Subject user) throws Exception {
		restUserMockMvc.perform(get("/api/users?sort=subjectId,desc&" + filter))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].subjectId").value(hasItem(user.getSubjectId())))
				.andExpect(jsonPath("$.[*].firstName").value(hasItem(user.getFirstName())))
				.andExpect(jsonPath("$.[*].lastName").value(hasItem(user.getLastName())));
	}
}
