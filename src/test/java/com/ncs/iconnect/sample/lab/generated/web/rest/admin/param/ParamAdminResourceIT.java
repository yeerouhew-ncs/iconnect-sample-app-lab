package com.ncs.iconnect.sample.lab.generated.web.rest.admin.param;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncs.iforge5.param.Constants;
import com.ncs.iforge5.param.repository.ParamRepository;
import com.ncs.iforge5.param.service.ParamService;
import com.ncs.iforge5.param.to.MapItemTO;
import com.ncs.iforge5.param.to.ParamDTO;
import com.ncs.iforge5.param.to.ParamTO;
import com.ncs.iforge5.param.to.ParamTOId;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ParamAdminResourceIT {
	
	@Autowired
    private WebApplicationContext wac;
	
	private MockMvc restParamAdminMockMvc;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private ParamService paramService;
	
	@Autowired
	private ParamRepository paramRepository;
	
	private static final String APP_ID = "IConnect";
	
	@BeforeEach
    public void setup() {
		MockitoAnnotations.initMocks(this);
		restParamAdminMockMvc= MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	private static ParamDTO createEntity(String paramKey) {
		ParamDTO paramDTO = new ParamDTO();
		ParamTOId paramId = new ParamTOId();
		paramId.setAppId(APP_ID);
		paramId.setParamKey(paramKey);
		paramDTO.setAppId(APP_ID);
		paramDTO.setId(paramId);
		return paramDTO;
	}
	
	@Test
	public void findParameters() throws Exception{
		ParamDTO paramDTO = createEntity("findParameters-UT");
		paramService.create(paramDTO);
		
		String url = "/api/paramadmin?appId="+paramDTO.getAppId()+"&paramKey="+paramDTO.getParamKey()
				+"&paramDesc=&effectiveDateAsStr=&expireDateAsStr=";
		
		restParamAdminMockMvc.perform(get(url))
				.andExpect(jsonPath("$.[*].paramKey").value(hasItem(paramDTO.getParamKey())));
	}
	
	@Test
	public void createParameter() throws Exception{
		ParamDTO paramDTO = createEntity("createParameters-UT");
		restParamAdminMockMvc.perform(post("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void updateParameter() throws Exception{
		ParamDTO paramDTO = createEntity("updateParameters-UT");
		paramService.create(paramDTO);
		
		ParamDTO updateDTO = paramService.getParamById(paramDTO.getId());
		updateDTO.setParamDesc("update param");
		
		restParamAdminMockMvc.perform(put("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(updateDTO)))
				.andExpect(status().isOk());
		
		ParamDTO existDTO = paramService.getParamById(updateDTO.getId());
		
		assertThat(existDTO.getParamDesc()).isEqualTo(updateDTO.getParamDesc());
	}
	
	
	
	@Test
	public void createParameterWithInvalidPath() throws Exception{
		ParamDTO paramDTO = createEntity("updateParameters UT without path");
		restParamAdminMockMvc.perform(post("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andReturn()
				.getResponse()
				.getContentAsString().isEmpty();
	}
	
	@Test
	public void createParameterWithDuplicatedKey() throws Exception{
		MapItemTO mTO1 = new MapItemTO();
		mTO1.setMapKey("");
		mTO1.setMapValue("");
		
		MapItemTO mTO2 = new MapItemTO();
		mTO2.setMapKey("k1");
		mTO2.setMapValue("v1");
		
		List<MapItemTO> paramMap = new ArrayList<MapItemTO>();
		paramMap.add(mTO1);
		paramMap.add(mTO2);
		
		ParamDTO paramDTO = createEntity("createParameters-UT-duplicated");
		paramDTO.setParamType(Constants.PARAM_TYPE.MAP);
		paramDTO.setParamMap(paramMap);
		
		restParamAdminMockMvc.perform(post("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateParameterWithDuplicatedKey() throws Exception{
		MapItemTO mTO1 = new MapItemTO();
		mTO1.setMapKey("");
		mTO1.setMapValue("");
		
		MapItemTO mTO2 = new MapItemTO();
		mTO2.setMapKey("k1");
		mTO2.setMapValue("v1");
		
		List<MapItemTO> paramMap = new ArrayList<MapItemTO>();
		paramMap.add(mTO1);
		paramMap.add(mTO2);
		
		ParamDTO paramDTO = createEntity("updateParameters-UT-duplicated");
		paramDTO.setParamType(Constants.PARAM_TYPE.MAP);
		paramDTO.setParamMap(paramMap);
		
		restParamAdminMockMvc.perform(put("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void createParameterWithExistKey() throws Exception{
		ParamDTO paramDTO = createEntity("createParameters-UT-exist");
		paramService.create(paramDTO);
		
		restParamAdminMockMvc.perform(post("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void createParmeterWithInvalidExpireDate() throws Exception {
		ParamDTO paramDTO = createEntity("createParameters-UT-invalid-date");
		paramDTO.setEffectiveDate(DateUtil.tomorrow());
		paramDTO.setExpireDate(DateUtil.now());
		
		restParamAdminMockMvc.perform(post("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateParmeterWithInvalidExpireDate() throws Exception {
		ParamDTO paramDTO = createEntity("updateParameters-UT-invalid-date");
		paramDTO.setEffectiveDate(DateUtil.tomorrow());
		paramDTO.setExpireDate(DateUtil.now());
		
		restParamAdminMockMvc.perform(put("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getAppList() throws Exception {
		restParamAdminMockMvc.perform(get("/api/applicationList"))
				.andExpect(jsonPath("$.[*].appId").value(hasItem(APP_ID)));
	}
	
	@Test
	public void findParameter() throws Exception{
		ParamDTO paramDTO = createEntity("findParameter-UT");
		paramService.create(paramDTO);
		
		restParamAdminMockMvc.perform(get("/api/paramadmin/{appId}/{paramKey}",paramDTO.getAppId(),paramDTO.getParamKey()))
				.andExpect(jsonPath("$.[*].appId").value(hasItem(APP_ID)));
		
	}
	
	@Test
	public void deleteParameter() throws Exception{
		ParamDTO paramDTO = createEntity("deleteParameter-UT");
		paramService.create(paramDTO);
		
		restParamAdminMockMvc.perform(delete("/api/paramadmin/{appId}/{paramKey}",paramDTO.getAppId(),paramDTO.getParamKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void deleteParameters() throws Exception{
		ParamDTO paramDTO = createEntity("deleteParameters-UT");
		restParamAdminMockMvc.perform(post("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramDTO)))
				.andExpect(status().isCreated());
		
		ParamDTO deleteDTO = createEntity("deleteParameters-UT");
		ParamTO paramTO = new ParamTO(deleteDTO);
		List<ParamTO> paramTOs = new ArrayList<ParamTO>();
		paramTOs.add(paramTO);
		
		
		restParamAdminMockMvc.perform(delete("/api/paramadmin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(paramTOs)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getAllParamKey() throws Exception {
		ParamDTO paramDTO = createEntity("getAllParamKey-UT");
		paramService.create(paramDTO);
		
		restParamAdminMockMvc.perform(get("/api/paramadmin/paramkeys?appId="+APP_ID))
				.andExpect(jsonPath("$.[*]").value(hasItem(paramDTO.getParamKey())));
	}
	
}
