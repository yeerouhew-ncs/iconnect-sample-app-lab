package com.ncs.iconnect.sample.lab.generated.web.rest.admin.code;

import static com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ncs.iforge5.codeadmin.domain.CodeInt;
import com.ncs.iforge5.codeadmin.dto.CodeIntDTO;
import com.ncs.iforge5.codeadmin.dto.CodeTypeDTO;
import com.ncs.iforge5.codeadmin.dto.Locale2DescMapDTO;
import com.ncs.iforge5.codeadmin.service.CodeAdminService;
import com.ncs.iframe5.code.CodeConstants;
import com.ncs.iframe5.commons.AppConfig;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iconnect.sample.lab.generated.web.rest.errors.ExceptionTranslator;
import com.ncs.itrust5.sss.dto.AppDTO;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class CodeAdminResourceIT {
	private MockMvc restCodeAdminMockMvc;
	
	@Mock
	private CodeAdminService codeAdminService;
	
	@Autowired
	private MessageSource msgSource;
	
	@Autowired
	private AppConfig appConfig; 
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
	
	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	private static final String APP_ID = "IConnect";
	private static String CODELOOK_UP_ACTIVE_STATUS = "A";
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		final CodeAdminResource codeAdminRecource = new CodeAdminResource(codeAdminService, msgSource, appConfig);
		this.restCodeAdminMockMvc = MockMvcBuilders.standaloneSetup(codeAdminRecource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService()).setMessageConverters(jacksonMessageConverter)
				.build();
	}
	
	private static CodeTypeDTO createCodeType(String prefix) {
		CodeTypeDTO codeTypeDTO = new CodeTypeDTO();
		codeTypeDTO.setAppId(APP_ID);
		codeTypeDTO.setCodeTypeId(prefix + "-type-UT-ID");
		codeTypeDTO.setColCodeDesc(prefix + "-type-UT-DESC");
		codeTypeDTO.setCodeTypePk(prefix + "cd_PK");
		codeTypeDTO.setCodeTypeTable(CodeConstants.DEFAULT_COLUMN_NAME_CODETYPE_TABLE);
		codeTypeDTO.setColCodeTypeId(CodeConstants.DEFAULT_COLUMN_NAME_COL_CODETYPE_ID );
		codeTypeDTO.setColCodeId(CodeConstants.DEFAULT_COLUMN_NAME_COL_CODE_ID);
		return codeTypeDTO;
	}
	
	private static CodeIntDTO createCodeInt(String prefix, String codeTypePK) {
		CodeIntDTO codeIntDTO = new CodeIntDTO();
		codeIntDTO.setId(prefix + "-code-id");
		codeIntDTO.setCodeId(prefix + "-code");
		codeIntDTO.setStatus(CODELOOK_UP_ACTIVE_STATUS);
		codeIntDTO.setCodeTypePK(codeTypePK);
		codeIntDTO.setCodeSeq(1);
		List<Locale2DescMapDTO> codeDescs = new ArrayList<Locale2DescMapDTO>();
		Locale2DescMapDTO locale2DescMapDTO = new Locale2DescMapDTO();
		locale2DescMapDTO.setId(prefix + "localeId");
		locale2DescMapDTO.setLocale("en");
		locale2DescMapDTO.setCodeDesc(prefix + "-locale-desc");
		codeDescs.add(locale2DescMapDTO);
		codeIntDTO.setCodeDescs(codeDescs);
		return codeIntDTO;
		
	}
	private static List<AppDTO> createAppDTOs(){
		AppDTO appDTO = new AppDTO();
		appDTO.setAppId(APP_ID);
		List<AppDTO> list = new ArrayList<AppDTO>() {{
			add(appDTO);
		}};
		return list;
	}
	
	@Test
	public void getAppList() throws Exception {
		when(codeAdminService.getAppList()).thenReturn(createAppDTOs());
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/appList"))
				.andExpect(jsonPath("$.[*].appId").value(hasItem(APP_ID)));
	}
	
	@Test 
	public void createCodeType() throws Exception {
		CodeTypeDTO dto = createCodeType("createCodeType");
		when(codeAdminService.createCodeType(any())).thenReturn(dto);
		restCodeAdminMockMvc.perform(post("/api/codeAdmin/codeType")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(dto)))
				.andExpect(jsonPath("$.appId").value(equalTo(dto.getAppId())));
	}
	
	@Test 
	public void createCodeTypeWithResourceUsageException() throws Exception {
		CodeTypeDTO dto = createCodeType("createCodeType");
		when(codeAdminService.createCodeType(any()))
				.thenThrow(new InvalidDataAccessResourceUsageException("Exception createCodeTypeWithResourceUsageException UT",
							new Exception("Exception createCodeTypeWithResourceUsageException UT")));
		restCodeAdminMockMvc.perform(post("/api/codeAdmin/codeType")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(dto)))
				.andExpect(status().isBadRequest());
	}
	
	@Test 
	public void updateCodeType() throws Exception {
		CodeTypeDTO updateDTO = createCodeType("updateCodeType");
		when(codeAdminService.editCodeType(any())).thenReturn(updateDTO);
		restCodeAdminMockMvc.perform(put("/api/codeAdmin/codeType")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.appId").value(equalTo(updateDTO.getAppId())));
	}
	@Test 
	public void updateCodeTypeWithResourceUsageException() throws Exception {
		CodeTypeDTO updateDTO = createCodeType("updateCodeType");
		when(codeAdminService.editCodeType(any()))
		.thenThrow(new InvalidDataAccessResourceUsageException("Exception createCodeTypeWithResourceUsageException UT",
				new Exception("Exception createCodeTypeWithResourceUsageException UT")));
		restCodeAdminMockMvc.perform(put("/api/codeAdmin/codeType")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updateDTO)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void findCodeTypes() throws Exception{
		CodeTypeDTO dto = createCodeType("findCodeTypes");
		List<CodeTypeDTO> content = new ArrayList<CodeTypeDTO>() {{
			add(dto);
		}};
		Page<CodeTypeDTO> page = new PageImpl<CodeTypeDTO>(content);
		when(codeAdminService.searchCodeType(any(), any())).thenReturn(page);
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/codeType?appId="+APP_ID+"&codeType="+dto.getCodeTypeId()))
				.andExpect(jsonPath("$.[*].codeTypeId").value(hasItem(dto.getCodeTypeId())));
	}
	
	@Test
	public void getCodeType() throws Exception{
		CodeTypeDTO dto = createCodeType("getCodeType");
		when(codeAdminService.findCodeTypeById(any())).thenReturn(dto);
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/codeType/{id}",dto.getCodeTypePk()))
				.andExpect(jsonPath("$.codeTypeId").value(equalTo(dto.getCodeTypeId())));
	}
	
	@Test
	public void deleteCodeType() throws Exception{
		CodeTypeDTO dto = createCodeType("deleteCodeType");
		restCodeAdminMockMvc.perform(delete("/api/codeAdmin/codeType/{codeTypePk}",dto.getCodeTypePk()))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getCodesByCodeTypePk() throws Exception{
		CodeTypeDTO typeDTO = createCodeType("getCodesByCodeTypePk");
		CodeIntDTO codeDTO = createCodeInt("getByCYPk",typeDTO.getCodeTypePk());
		List<CodeIntDTO> codes = new ArrayList<CodeIntDTO>() {{
			add(codeDTO);
		}};
		when(codeAdminService.getCodesByInternalCodeTypeGroupByCodeId(anyString())).thenReturn(codes);
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/codesByCodeTypePk?codeTypePk="+typeDTO.getCodeTypePk()+"&isExternal=false"))
				.andExpect(jsonPath("$.[*].codeId").value(hasItem(codeDTO.getCodeId())));
	}
	
	@Test
	public void getCodesByCodeTypePkAndCodeId() throws Exception{
		CodeTypeDTO typeDTO = createCodeType("getCodesByCodeTypePkAndCodeId");
		CodeIntDTO codeDTO = createCodeInt("gPkAId",typeDTO.getCodeTypePk());
		List<CodeIntDTO> codes = new ArrayList<CodeIntDTO>() {{
			add(codeDTO);
		}};
		when(codeAdminService.findByCodeTypeIdAndCodeId(anyString(), anyString())).thenReturn(codes);
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/codesByCodeTypePkAndCodeId?codeTypePk="+typeDTO.getCodeTypePk()+"&codeId="+codeDTO.getCodeId()))
				.andExpect(jsonPath("$.[*].codeId").value(hasItem(codeDTO.getCodeId())));
	}
	
	@Test
	public void getCodeTypesByAppIdAndCodeTypePkNot() throws Exception{
		CodeTypeDTO typeDTO = createCodeType("getCodeTypesByAppIdAndCodeTypePkNot");
		CodeIntDTO codeDTO = createCodeInt("gPkAIdN",typeDTO.getCodeTypePk());
		List<CodeTypeDTO> codes = new ArrayList<CodeTypeDTO>() {{
			add(typeDTO);
		}};
		when(codeAdminService.getInternalCodeTypesByAppIdAndCodeTypePkNot(any(), any())).thenReturn(codes);
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/codeTypesByAppIdAndCodeTypePkNot?codeTypePk="+typeDTO.getCodeTypePk()+"&codeId="+codeDTO.getCodeId()))
				.andExpect(jsonPath("$.[*].codeTypePk").value(hasItem(codeDTO.getCodeTypePK())));
	}
	
	@Test
	public void getCode() throws Exception{
		CodeTypeDTO typeDTO = createCodeType("getCode");
		CodeIntDTO code = createCodeInt("getCode", typeDTO.getCodeTypePk());
		when(codeAdminService.findCodeById(anyString())).thenReturn(code);
		restCodeAdminMockMvc.perform(get("/api/codeAdmin/code/{id}", code.getId()))
				.andExpect(jsonPath("$.codeTypePK").value(equalTo(code.getCodeTypePK())));
	}
	
	@Test
	public void createCode() throws Exception{
		CodeTypeDTO typeDTO = createCodeType("createCode");
		CodeIntDTO code = createCodeInt("getCode", typeDTO.getCodeTypePk());
		
		when(codeAdminService.findByCodeTypeIdAndCodeIdAndLocale(anyString(), anyString(), anyString()))
				.thenReturn(new ArrayList<CodeInt>());
		when(codeAdminService.createCode(any())).thenReturn(code);
		
		restCodeAdminMockMvc.perform(post("/api/codeAdmin/code")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(code)))
				.andExpect(jsonPath("$.codeId").value(equalTo(code.getCodeId())));
							
	}
	
	@Test
	public void updateCode() throws Exception{
		CodeTypeDTO typeDTO = createCodeType("updateCode");
		CodeIntDTO code = createCodeInt("getCode", typeDTO.getCodeTypePk());
		
		when(codeAdminService.editCodes(any())).thenReturn(code);
		
		restCodeAdminMockMvc.perform(put("/api/codeAdmin/code")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(code)))
		.andExpect(jsonPath("$.codeId").value(equalTo(code.getCodeId())));
		
	}
	
	@Test
	public void deleteCode() throws Exception {
		CodeTypeDTO typeDTO = createCodeType("deleteCode");
		CodeIntDTO code = createCodeInt("getCode", typeDTO.getCodeTypePk());
		when(codeAdminService.findByCodeTypeIdAndCodeId(anyString(), anyString())).thenReturn(
				new ArrayList<CodeIntDTO>(){{
					add(code);
				}});
		restCodeAdminMockMvc.perform(delete("/api/codeAdmin/code?codeTypePk="+code.getCodeTypePK()+"&codeId="+code.getCodeId()))
			.andExpect(status().isOk());
	}
}
