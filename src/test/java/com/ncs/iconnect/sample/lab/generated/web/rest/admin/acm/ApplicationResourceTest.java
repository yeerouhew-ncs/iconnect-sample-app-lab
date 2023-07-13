package com.ncs.iconnect.sample.lab.generated.web.rest.admin.acm;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import com.ncs.itrust5.acm.app.service.AcmApplicationService;
import com.ncs.itrust5.ss5.domain.Application;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ApplicationResourceTest {
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	private MockMvc restApplicationResourceMockMvc;
	
	@Mock
	private AcmApplicationService amcApplicationService;
	
	private static final String APP_ID = "IConnect";
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final ApplicationResource resourceResource = new ApplicationResource(amcApplicationService);
		this.restApplicationResourceMockMvc = MockMvcBuilders.standaloneSetup(resourceResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService()).setMessageConverters(jacksonMessageConverter)
				.build();
	}
	
	private Application createApp() {
		Application app = new Application();
		app.setAppId(APP_ID);
		app.setAppCode(APP_ID);
		app.setAppName(APP_ID);
		return app;
	}
	
	
	@Test
	public void createApplication() throws Exception {
		Application app = createApp();
		when(amcApplicationService.findByAppCode(anyString())).thenReturn(new ArrayList<Application>());
		when(amcApplicationService.save(any())).thenReturn(app);
		when(amcApplicationService.prepareApplication(any())).thenReturn(app);
		restApplicationResourceMockMvc.perform(post("/api/applications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(app)))
				.andExpect(jsonPath("$.appCode").value(equalTo(app.getAppCode())));
	}
	
	@Test
	public void updateApplication() throws Exception {
		Application app = createApp();
		when(amcApplicationService.save(any())).thenReturn(app);
		when(amcApplicationService.prepareApplication(any())).thenReturn(app);
		restApplicationResourceMockMvc.perform(put("/api/applications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(app)))
				.andExpect(jsonPath("$.appCode").value(equalTo(app.getAppCode())));
	}
	
	@Test
	public void findApplications() throws Exception {
		Application app = createApp();
		List<Application> content = new ArrayList<Application>() {{
			add(app);
		}};
		Page<Application> page = new PageImpl<Application>(content);
		when(amcApplicationService.findApplication(any(), any(), any(), any()))
		 		.thenReturn(page);
		when(amcApplicationService.prepareApplication(any())).thenReturn(app);
		
		restApplicationResourceMockMvc.perform(get("/api/applications?appCode="+APP_ID))
		.andExpect(jsonPath("$.[*].appCode").value(hasItem(app.getAppCode())));
	}
	
	@Test
	public void findApplication() throws Exception {
		Application app = createApp();
		when(amcApplicationService.find(anyString())).thenReturn(app);
		when(amcApplicationService.prepareApplication(any())).thenReturn(app);
		
		restApplicationResourceMockMvc.perform(get("/api/applications/{id}" ,APP_ID))
		.andExpect(jsonPath("$.appCode").value(equalTo(app.getAppCode())));
	}
	
}
