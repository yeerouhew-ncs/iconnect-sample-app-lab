package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import com.jayway.jsonpath.JsonPath;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.acm.user.service.UserService;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.domain.Application;
import com.ncs.itrust5.ss5.domain.Group;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.itrust5.ss5.domain.Subject;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class RoleResourceIT {
	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AcmApplicationService acmAppService;
	
	private MockMvc restRoleMockMvc;
	
	@Autowired
	private OpenEntityManagerInViewFilter openEntityManagerInViewFilter;
	
	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
	
	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;
	
	private Resource resource;
	
	private static final String APP_ID = "IConnect";
	private static final String CONFIG_ROLE = "DEF-role-configadmin";
	private static final String CONFIG_USER = "DEF-user-configadmin";
	private static final String CONFIG_FUNC = "DEF-func-acm-delegate";
	private static final String UNIVERSAL_GROUP = "REQ-group-universal";
	private static final String EXTRANET_GROUP = "REQ-group-extranet";
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final RoleResource roleRecource = new RoleResource(roleService, groupService, userService, acmAppService);
		this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleRecource)
				.addFilters(openEntityManagerInViewFilter)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService())
				.setMessageConverters(jacksonMessageConverter)
				.build();
	}
	
	@BeforeEach
	public void initTest() {
		resource = createEntity();
	}
	
	public static Resource createEntity() {
		Resource res = new Resource();
		res.setResourceName("Resource UT");
		res.setResourceType(ITrustConstants.RES_TYPE_URI);
		Application app = new Application();
		app.setAppId(APP_ID);
		app.setAppCode(APP_ID);
		app.setAppName(APP_ID);
		res.setApplication(app);
		return res;
	}
	
	@Test
	@Transactional
	public void createRole() throws Exception {
		Resource roleTO = (Resource) BeanUtils.cloneBean(resource);
		String responseString = restRoleMockMvc.perform(post("/api/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roleTO)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String resourceId = JsonPath.parse(responseString).read("$.resourceId");
		
		restRoleMockMvc.perform(get("/api/roles/{id}", resourceId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resourceName").value(equalTo(roleTO.getResourceName())));
	}
	
	@Test
	@Transactional
	public void updateRole() throws Exception{
		Resource roleTO = (Resource) BeanUtils.cloneBean(resource);
		roleTO.setResourceName("Update Role");
		String responseString = restRoleMockMvc.perform(post("/api/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roleTO)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String resourceId = JsonPath.parse(responseString).read("$.resourceId");
		
		Resource updateRole = (Resource) BeanUtils.cloneBean(resource);
		updateRole.setResourceId(resourceId);
		updateRole.setResourceName("Update Resource Name");
		
		restRoleMockMvc.perform(put("/api/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updateRole)))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/{id}", resourceId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resourceName").value(equalTo(updateRole.getResourceName())));
	}
	
	@Test 
	public void findAllApplication() throws Exception {
		restRoleMockMvc.perform(get("/api/roles/applications"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void findRoles() throws Exception  {
		Resource roleTO = (Resource) BeanUtils.cloneBean(resource);
		roleTO.setResourceName("Find Roles");
		restRoleMockMvc.perform(post("/api/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roleTO)))
				.andExpect(status().isCreated());
		
		restRoleMockMvc.perform(get("/api/roles?sort=resourceId,desc&applicationId="+roleTO.getAppId()+"&roleName="+roleTO.getResourceName()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].resourceName").value(hasItem(roleTO.getResourceName())));
	}
	
	@Test
	@Transactional
	public void deleteRole() throws Exception {
		Resource roleTO = (Resource) BeanUtils.cloneBean(resource);
		roleTO.setResourceName("Delete Role");
		String responseString = restRoleMockMvc.perform(post("/api/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roleTO)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String resourceId = JsonPath.parse(responseString).read("$.resourceId");
		
		restRoleMockMvc.perform(delete("/api/roles/{id}",resourceId))
				.andExpect(status().isOk());
	}
	
	@Test
	public void findAllAssignedUsers() throws Exception {
		restRoleMockMvc.perform(get("/api/roles/assignedUsers/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].subjectId").value(hasItem(CONFIG_USER)));
	}
	
	@Test
	public void findAllAssignedFuncs() throws Exception {
		restRoleMockMvc.perform(get("/api/roles/assignedFuncs/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].resourceId").value(hasItem(CONFIG_FUNC)));
	}
	
	@Test
	public void findAllAssignedGroups() throws Exception {
		restRoleMockMvc.perform(get("/api/roles/assignedGroups/{id}", CONFIG_ROLE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void assignAndUnAssignUser() throws Exception {
		Subject sbj = new Subject();
		sbj.setSubjectId(CONFIG_USER);
		Subject[] sbjs = new Subject[1];
		sbjs[0] = sbj;
		restRoleMockMvc.perform(put("/api/roles/unAssignUsers/{resourceId}", CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(sbjs)))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/search/unAssignedUsers?resourceId=" + CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/assignedUsers/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].subjectId").value(not(hasItem(CONFIG_USER))));
		
		restRoleMockMvc.perform(put("/api/roles/assignUsers/{resourceId}", CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(sbjs)))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/assignedUsers/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].subjectId").value(hasItem(CONFIG_USER)));
		
	}
	
	@Test
	public void assignAndUnAssignFuncs() throws Exception {
		Resource func = new Resource();
		func.setResourceId(CONFIG_FUNC);
		Resource[] funcs = new Resource[1];
		funcs[0] = func;
		restRoleMockMvc.perform(put("/api/roles/unAssignFuncs/{resourceId}", CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(funcs)))
		.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/search/unAssignedFuncs?resourceId=" + CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/assignedFuncs/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].resourceId").value(not(hasItem(CONFIG_FUNC))));
		
		restRoleMockMvc.perform(put("/api/roles/assignFuncs/{resourceId}", CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(funcs)))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/assignedFuncs/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].resourceId").value(hasItem(CONFIG_FUNC)));
		
	}
	
	@Test
	public void assignAndUnAssignGroups() throws Exception {
		Group grp = new Group();
		grp.setGroupId(EXTRANET_GROUP);
		Group[] grps = new Group[1];
		grps[0] = grp;
		
		restRoleMockMvc.perform(get("/api/roles/assignedGroups/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].groupId").value(not(hasItem(EXTRANET_GROUP))));
		
		restRoleMockMvc.perform(get("/api/roles/search/unAssignedGroups?resourceId=" + CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(put("/api/roles/assignGroups/{resourceId}", CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grps)))
				.andExpect(status().isOk());
		
		restRoleMockMvc.perform(get("/api/roles/assignedGroups/{id}", CONFIG_ROLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].groupId").value(hasItem(EXTRANET_GROUP)));

		restRoleMockMvc.perform(put("/api/roles/unAssignGroups/{resourceId}", CONFIG_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grps)))
				.andExpect(status().isOk());
		
	}
	
	
}