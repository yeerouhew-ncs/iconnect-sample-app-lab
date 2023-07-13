package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.acm.function.service.ResourceService;
import com.ncs.itrust5.ss5.ITrustConstants;
import com.ncs.itrust5.ss5.domain.Application;
import com.ncs.itrust5.ss5.domain.Resource;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ResourceResourceIT {
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restResourceResourceMockMvc;
	
	@Mock
    private ResourceService resourceService;

	@Mock
    private AcmApplicationService acmAppService;
	
	private static final String APP_ID = "IConnect";
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final ResourceResource resourceResource = new ResourceResource(resourceService, acmAppService);
		this.restResourceResourceMockMvc = MockMvcBuilders.standaloneSetup(resourceResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService()).setMessageConverters(jacksonMessageConverter)
				.build();
	}
	
	public static Resource createEntity() {
		Resource res = new Resource();
		res.setResourceName("Function UT");
		res.setResourceType(ITrustConstants.RES_TYPE_FUNCTION);
		Application app = new Application();
		app.setAppId(APP_ID);
		app.setAppCode(APP_ID);
		app.setAppName(APP_ID);
		res.setApplication(app);
		return res;
	}
	
	private static Pageable createPageable() {
		return new Pageable() {
			@Override
			public Pageable previousOrFirst() {
				return null;
			}
			
			@Override
			public Pageable next() {
				return null;
			}
			
			@Override
			public boolean hasPrevious() {
				return false;
			}
			
			@Override
			public Sort getSort() {
				return null;
			}
			
			@Override
			public int getPageSize() {
				return 0;
			}
			
			@Override
			public int getPageNumber() {
				return 0;
			}
			
			@Override
			public long getOffset() {
				return 0;
			}
			
			@Override
			public Pageable first() {
				return null;
			}
		};
	}
	
	@Test
	public void createResource() throws Exception {
		Resource res = createEntity();
		when(resourceService.findResourcesByResourceTypeAndResourceName(anyString(), anyString(), anyString()))
				.thenReturn(new ArrayList<Resource>());
		when(resourceService.save(any(Resource.class))).thenReturn(res);
		when(resourceService.prepareResource(any())).thenReturn(res);

		restResourceResourceMockMvc.perform(post("/api/resources").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res)))
				.andExpect(jsonPath("$.resourceName").value(equalTo(res.getResourceName())));
	}
	
	@Test
	public void updateResource() throws Exception {
		Resource res = createEntity();
		when(resourceService.findResourcesByResourceTypeAndResourceName(anyString(), anyString(), anyString()))
				.thenReturn(new ArrayList<Resource>());
		when(resourceService.save(any(Resource.class))).thenReturn(res);
		when(resourceService.prepareResource(any())).thenReturn(res);

		restResourceResourceMockMvc.perform(put("/api/resources").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(res)))
				.andExpect(jsonPath("$.resourceName").value(equalTo(res.getResourceName())));
	}
	
	@Test
	public void findAllApplication() throws Exception {
		Application app = new Application();
		app.setAppCode(APP_ID);
		List<Application> result = new ArrayList<Application>() {{
			add(app);
		}};
		when(acmAppService.getAllApplications()).thenReturn(result);
		restResourceResourceMockMvc.perform(get("/api/resources/applications"))
				.andExpect(jsonPath("$.[*].appCode").value(hasItem(app.getAppCode())));
	}
	
	@Test
	public void findResources() throws Exception{
		Resource res = createEntity();
		Page<Resource> resultPage = new PageImpl<Resource>(new ArrayList<Resource>() {{
			add(res);
		}});
		when(resourceService.findResources(anyString(), anyString(), anyString(), anyString(), anyString(), any()))
				.thenReturn(resultPage);
		restResourceResourceMockMvc.perform(get("/api/resources?applicationId=applicationId"))
		.andDo(print());
	}
	
	@Test
	public void getResource() throws Exception{
		Resource res = createEntity();
		
		when(resourceService.find(anyString())).thenReturn(res);
		when(resourceService.prepareResource(any())).thenReturn(res);
		
		restResourceResourceMockMvc.perform(get("/api/resources/{id}", "id"))
				.andExpect(jsonPath("$.resourceName").value(equalTo(res.getResourceName())));
	}
	
	@Test
	public void deleteFunction() throws Exception{
		Resource res = createEntity();
		when(resourceService.find(anyString())).thenReturn(res);
		restResourceResourceMockMvc.perform(delete("/api/resources/{id}","id"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void unAssignFunctions() throws Exception{
		Resource[] rsArray = {createEntity()};
		restResourceResourceMockMvc.perform(put("/api/resources/unAssignFunctions/{resourceId}","id")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(rsArray)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void findUnAssignFunctions() throws Exception{
		Resource res = createEntity();
		Page<Resource> resultPage = new PageImpl<Resource>(new ArrayList<Resource>() {{
			add(res);
		}},createPageable(),10);
		when(resourceService.findUnassignedFunctions(any(), any(), any(), any(), any()))
				.thenReturn(resultPage);
		restResourceResourceMockMvc.perform(get("/api/resources/search/unAssignFunctions?applicationId=applicationId"))
				.andExpect(jsonPath("$.[*].resourceName").value(hasItem(res.getResourceName())));
	}
	
	@Test
	public void assignFunctions() throws Exception{
		Resource[] rsArray = {createEntity()};
		restResourceResourceMockMvc.perform(put("/api/resources/assignFunctions/{resourceId}","id")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(rsArray)))
				.andExpect(status().isOk());
	}

}
