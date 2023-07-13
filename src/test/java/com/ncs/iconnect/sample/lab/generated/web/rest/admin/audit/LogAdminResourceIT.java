package com.ncs.iconnect.sample.lab.generated.web.rest.admin.audit;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.ncs.iforge5.logadmin.domain.ViolationLogTO;
import com.ncs.iforge5.logadmin.service.AuditLogService;
import com.ncs.iforge5.logadmin.service.ViolationLogService;
import com.ncs.iforge5.logadmin.to.DownloadFile;
import com.ncs.iforge5.logadmin.to.RevisionPageItemTO;
import com.ncs.iframe5.commons.util.DateFormatter;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class LogAdminResourceIT {
	private MockMvc restLogAdminMockMvc;
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
	
	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	@Mock
	private AuditLogService auditLogService;
	
	@Mock
	private ViolationLogService violationLogService;
	
	private static final String APP_CODE = "IConnect";
	
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final LogAdminResource logAdminRecource = new LogAdminResource();
		logAdminRecource.setAuditLogService(auditLogService);
		logAdminRecource.setViolationLogService(violationLogService);
		this.restLogAdminMockMvc = MockMvcBuilders.standaloneSetup(logAdminRecource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService())
				.setMessageConverters(jacksonMessageConverter)
				.build();
	}
	
	@Test
	public void searchRevision() throws Exception {
		RevisionPageItemTO itemTO = new RevisionPageItemTO();
		itemTO.setRevision("revison");
		List<RevisionPageItemTO> content = new ArrayList<RevisionPageItemTO>() {{
			add(itemTO);
		}};
		Page<RevisionPageItemTO> page = new PageImpl<>(content);
		when(auditLogService.searchRevision(anyObject(), anyObject())).thenReturn(page);
		String fromDateAsStr = DateFormatter.formatDate(new Date());
		String toDateAsStr = DateFormatter.formatDate(new Date());
		String url = "/api/log/auditLog?fromDateAsStr="+fromDateAsStr+"&toDateAsStr="+toDateAsStr
				+"&revision=1&userId=userId&revisionType=revisionType"
				+ "&funcName=funcName&tableName=tableName"
				+ "&businessKey=businessKey";
		restLogAdminMockMvc.perform(get(url))
				.andExpect(jsonPath("$.[*].revision").value(hasItem(itemTO.getRevision())));
	}
	
	@Test
	public void getDownloadFile() throws Exception {
		DownloadFile downloadFile = new DownloadFile();
		downloadFile.setName("file");
		downloadFile.setSize(new Long(100));
		downloadFile.setType("type");
		when(auditLogService.getDownloadFile(anyObject())).thenReturn(downloadFile);
		String fromDateAsStr = DateFormatter.formatDate(new Date());
		String toDateAsStr = DateFormatter.formatDate(new Date());
		String url = "/api/log/downloadFile?fromDateAsStr="+fromDateAsStr+"&toDateAsStr="+toDateAsStr
				+"&revision=1&userId=userId&revisionType=revisionType"
				+ "&funcName=funcName&tableName=tableName"
				+ "&businessKey=businessKey";
		restLogAdminMockMvc.perform(get(url))
				.andExpect(jsonPath("$.name").value(equalTo(downloadFile.getName())));
	}
	
	@Test
	public void searchViolationLog() throws Exception {
		ViolationLogTO violationLogTO = new ViolationLogTO();
		violationLogTO.setAppCode(APP_CODE);
		Page<ViolationLogTO> page = new PageImpl<>(new ArrayList<ViolationLogTO>() {{
			add(violationLogTO);
		}});
		when(violationLogService.searchViolationLog(anyObject(), anyObject())).thenReturn(page);
		String fromDateAsStr = DateFormatter.formatDate(new Date());
		String toDateAsStr = DateFormatter.formatDate(new Date());
		String url = "/api/log/violationAccess?fromDateAsStr="+fromDateAsStr+"&toDateAsStr="+toDateAsStr+"&loginName=loginName";
		restLogAdminMockMvc.perform(get(url))
			.andExpect(jsonPath("$.content.[*].appCode").value(hasItem(violationLogTO.getAppCode())));
	}
}
