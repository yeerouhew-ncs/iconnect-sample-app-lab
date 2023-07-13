package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import com.ncs.itrust5.acm.group.service.GroupService;
import com.ncs.itrust5.acm.role.service.RoleService;
import com.ncs.itrust5.acm.user.service.UserService;
import com.ncs.itrust5.ss5.domain.Group;
import com.ncs.itrust5.ss5.domain.Resource;
import com.ncs.itrust5.ss5.domain.Subject;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class GroupResourceIT {
	@Autowired
	private GroupService groupService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private RoleService roleService;
	
	private MockMvc restGroupMockMvc;
	
	@Autowired
	private OpenEntityManagerInViewFilter openEntityManagerInViewFilter;
	
	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
	
	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;
	
	private Group group;
	
	@Autowired
	private EntityManager em;
	
	private static final String CONFIG_USER = "DEF-user-configadmin";
	private static final String UNIVERSAL_GROUP = "REQ-group-universal";
	private static final String CONFIG_ROLE = "DEF-role-configadmin";
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final GroupResource groupResource = new GroupResource(groupService, userService, roleService);
		this.restGroupMockMvc = MockMvcBuilders.standaloneSetup(groupResource)
				.addFilters(openEntityManagerInViewFilter)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService())
				.setMessageConverters(jacksonMessageConverter)
				.build();
	}
	
	@BeforeEach
	public void initTest() {
		group = createEntity();
	}

	private static Group createEntity() {
		Group grp = new Group();
		grp.setGroupName("Group UT");
		return grp;
	}
	
	@Test
	@Transactional
	public void createGroup() throws Exception{
		Group grpTO = (Group) BeanUtils.cloneBean(group);
		grpTO.setGroupId("Create-Group-UT");
		String responseString = restGroupMockMvc.perform(post("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grpTO)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String groupId = JsonPath.parse(responseString).read("$.groupId");
		
		restGroupMockMvc.perform(get("/api/groups/{id}", groupId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.groupName").value(equalTo(grpTO.getGroupName())));
	}
	
	@Test
	@Transactional
	public void updateGroup() throws Exception{
		Group grpTO = (Group) BeanUtils.cloneBean(group);
		grpTO.setGroupId("Update-Group-UT");
		grpTO.setGroupName("Update Groups");
		String responseString = restGroupMockMvc.perform(post("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grpTO)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String groupId = JsonPath.parse(responseString).read("$.groupId");
		
		Group updateGrp = groupService.find(groupId);
		updateGrp.setGroupName("Update Group Name");
		em.detach(updateGrp);
		
		restGroupMockMvc.perform(put("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updateGrp)))
				.andExpect(status().isOk());
		
		restGroupMockMvc.perform(get("/api/groups/{id}", groupId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.groupName").value(equalTo(updateGrp.getGroupName())));
	}
	
	@Test
	public void findGroups() throws Exception  {
		Group grpTO = (Group) BeanUtils.cloneBean(group);
		grpTO.setGroupId("Find-Group-UT");
		grpTO.setGroupName("Find Groups");
		restGroupMockMvc.perform(post("/api/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(grpTO)))
				.andExpect(status().isCreated());
		
		restGroupMockMvc.perform(get("/api/groups?sort=groupId,desc&groupId="+grpTO.getGroupId()+"&groupName="+grpTO.getGroupName()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].groupName").value(hasItem(grpTO.getGroupName())));
	}
	
	@Test
	@Transactional
	public void deleteGroup() throws Exception{
		Group grpTO = (Group) BeanUtils.cloneBean(group);
		grpTO.setGroupId("Delete-Group-UT");
		grpTO.setGroupName("Delete Groups");
		
		groupService.save(grpTO);
		
		restGroupMockMvc.perform(delete("/api/groups/{id}",grpTO.getGroupId()))
			.andExpect(status().isOk());
	}
	
	@Test
	public void findAllAssignedUsers() throws Exception {
		restGroupMockMvc.perform(get("/api/groups/assignedUsers/{id}", UNIVERSAL_GROUP))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].subjectId").value(hasItem(CONFIG_USER)));
	}
	
	@Test
	public void findAllAssignedRoles() throws Exception {
		restGroupMockMvc.perform(get("/api/groups/assignedRoles/{id}", UNIVERSAL_GROUP))
				.andExpect(status().isOk());
	}
	
	@Test
	public void assignAndUnAssignUser() throws Exception {
		Subject sbj = new Subject();
		sbj.setSubjectId(CONFIG_USER);
		Subject[] sbjs = new Subject[1];
		sbjs[0] = sbj;
		restGroupMockMvc.perform(put("/api/groups/unAssignUsers/{groupId}", UNIVERSAL_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(sbjs)))
				.andExpect(status().isOk());
		
		restGroupMockMvc.perform(get("/api/groups/search/unAssignedUsers?groupId=" + UNIVERSAL_GROUP)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		restGroupMockMvc.perform(get("/api/groups/assignedUsers/{id}", UNIVERSAL_GROUP))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].subjectId").value(not(hasItem(CONFIG_USER))));
		
		restGroupMockMvc.perform(put("/api/groups/assignUsers/{groupId}", UNIVERSAL_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(sbjs)))
				.andExpect(status().isOk());
		
		restGroupMockMvc.perform(get("/api/groups/assignedUsers/{id}", UNIVERSAL_GROUP))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].subjectId").value(hasItem(CONFIG_USER)));
	}
	
	@Test
	public void assignAndUnAssignRoles() throws Exception {
		Resource rs = new Resource();
		rs.setResourceId(CONFIG_ROLE);
		Resource[] roles = new Resource[1];
		roles[0] = rs;
		
		restGroupMockMvc.perform(get("/api/groups/assignedRoles/{id}", UNIVERSAL_GROUP))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].resourceId").value(not(CONFIG_ROLE)));
		
		restGroupMockMvc.perform(put("/api/groups/assignRoles/{groupId}", UNIVERSAL_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roles)))
				.andExpect(status().isOk());
		
		restGroupMockMvc.perform(get("/api/groups/assignedRoles/{id}", UNIVERSAL_GROUP))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[*].resourceId").value(hasItem(CONFIG_ROLE)));
		
		restGroupMockMvc.perform(put("/api/groups/unAssignRoles/{groupId}", UNIVERSAL_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(roles)))
				.andExpect(status().isOk());
		
		restGroupMockMvc.perform(get("/api/groups/search/unAssignedRoles?groupId=" + UNIVERSAL_GROUP)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	
}
