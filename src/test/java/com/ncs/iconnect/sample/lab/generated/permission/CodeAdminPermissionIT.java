package com.ncs.iconnect.sample.lab.generated.permission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;
import com.ncs.iforge5.codeadmin.dto.CodeIntDTO;
import com.ncs.iforge5.codeadmin.dto.CodeTypeDTO;
import com.ncs.iforge5.codeadmin.service.CodeAdminService;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class CodeAdminPermissionIT {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private CodeAdminService codeAdminService;

	@Autowired
    private TestTokenProvider testTokenProvider;
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
    
	private MockMvc mockMvc;
    private MediaType mediaType = MediaType.APPLICATION_JSON;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}

    @Test
    public void testCodeAdminGetAppListSuccess() throws Exception{
    	ResultActions result = mockMvc
    			.perform(get("/api/codeAdmin/appList")
        		.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
        
        result.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$.[*].appCode").value(hasItem("IConnect")));
    }
    
    @Test
    public void testCodeAdminGetAppListFailure() throws Exception{
    	ResultActions result = mockMvc
    			.perform(get("/api/codeAdmin/appList")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dummyUser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testCodeAdminCreateCodeTypeSuccess() throws Exception {
    	CodeTypeDTO codeType = new CodeTypeDTO();
    	codeType.setCodeTypePk("ncs_cc_id");
    	codeType.setAppId("IConnect");
    	codeType.setCodeTypeId("ncs_cc");
    	codeType.setCodeTypeDesc("test code type");
    	codeType.setCodeTypeTable("TBL_CODE_INT");
    	codeType.setColCodeTypeId("CODETYPE_ID");
    	codeType.setColCodeId("CODE_ID");
    	codeType.setColCodeDesc("CODE_DESC");
    	codeType.setColCodeSeq("CODE_SEQ");
    	codeType.setColLocale("LOCALE");
    	
    	ResultActions result = mockMvc.perform(post("/api/codeAdmin/codeType")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeType))
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
        
    	//restore the DB status
    	final String codeTypeObject = result.andReturn().getResponse().getContentAsString();
    	
    	JSONObject obj  = new JSONObject(codeTypeObject);
    	final String codeTypeId = obj.getString("codeTypePk");
    	
        result.andDo(print())
        	.andExpect(status().isCreated())
	        .andDo(new ResultHandler(){
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(delete("/api/codeAdmin/codeType/"+codeTypeId)
			        		.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
				}
	        });
    }
    
	@Test
    public void testCodeAdminCreateCodeTypeUnAuthorized() throws Exception {
    	CodeTypeDTO codeType = new CodeTypeDTO();
    	codeType.setCodeTypePk("ncs_cc_id");
    	codeType.setAppId("IConnect");
    	codeType.setCodeTypeId("ncs_cc");
    	codeType.setCodeTypeDesc("test code type");
    	codeType.setCodeTypeTable("TBL_CODE_INT");
    	codeType.setColCodeTypeId("CODETYPE_ID");
    	codeType.setColCodeId("CODE_ID");
    	codeType.setColCodeDesc("CODE_DESC");
    	codeType.setColCodeSeq("CODE_SEQ");
    	codeType.setColLocale("LOCALE");
    	
    	ResultActions result = mockMvc.perform(post("/api/codeAdmin/codeType")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeType))
        		.with(httpBasic("dummyuser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testCodeAdminUpdateCodeTypeSuccess() throws Exception{
    	CodeTypeDTO codeType = this.codeAdminService.findCodeTypeById("loginType");
    	assertThat(codeType).isNotNull();
    	codeType.setCodeTypeTable("TBL_CODE_INT");
    	codeType.setColCodeTypeId("CODETYPE_ID");
    	codeType.setColCodeId("CODE_ID");
    	
    	ResultActions result = mockMvc.perform(put("/api/codeAdmin/codeType")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeType))
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
    	
    	result.andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void testCodeAdminUpdateCodeTypeUnthorized() throws Exception{
    	CodeTypeDTO codeType = this.codeAdminService.findCodeTypeById("loginType");
    	assertThat(codeType).isNotNull();
    	codeType.setCodeTypeTable("TBL_CODE_INT");
    	codeType.setColCodeTypeId("CODETYPE_ID");
    	codeType.setColCodeId("CODE_ID");
    	
    	ResultActions result = mockMvc.perform(put("/api/codeAdmin/codeType")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeType))
        		.with(httpBasic("dummyadmin","password1")));
    	
    	result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    
    @Test
    public void testGetCodeTypesWithAuthorizedUserExpectSuccess() throws Exception {

        // when
        ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codeType/?appId=&codeType=loginType&codeTypeDesc=loginType&page=1&size=10&sort=")
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
        // then
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCodeTypesWithUnAuthorized() throws Exception {
        ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codeType/?appId=IConnect&codeType=loginType&codeTypeDesc=loginType&page=1&size=10&sort=")
        		.with(httpBasic("dummyUser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetCodeTypeSuccess() throws Exception{
    	ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codeType/country")
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
    	
    	result.andDo(print())
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("$.codeTypeId").value(equalTo("country")));
    }
    
    @Test
    public void testGetCodeTypeUnthorized() throws Exception{
    	ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codeType/country")
        		.with(httpBasic("dummyuser","password1")));
    	
    	result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testDeleteCodeTypeWithUnAuthorized() throws Exception{
    	CodeTypeDTO codeType = new CodeTypeDTO();
    	codeType.setAppId("IConnect");
    	codeType.setCodeTypeId("ncs_cc3");
    	codeType.setCodeTypeDesc("test code type");
    	codeType.setCodeTypeTable("TBL_CODE_INT");
    	codeType.setColCodeTypeId("CODETYPE_ID");
    	codeType.setColCodeId("CODE_ID");
    	codeType.setColCodeDesc("CODE_DESC");
    	codeType.setColCodeSeq("CODE_SEQ");
    	codeType.setColLocale("LOCALE");
    	
    	final CodeTypeDTO createdCodeType = codeAdminService.createCodeType(codeType);
    	
    	ResultActions result = mockMvc
        		.perform(delete("/api/codeAdmin/codeType/"+codeType.getCodeTypePk())
        		.with(httpBasic("dumyadmin","password1")));
    	
    	result.andDo(print()).andExpect(status().isUnauthorized())
	    	.andDo(new ResultHandler() {
				@Override
				//success delete just create by codeAdminService with Authorized success
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(delete("/api/codeAdmin/codeType/"+createdCodeType.getCodeTypePk())
			        		.with(httpBasic("configadmin","password1")));
				}
	    	});
    }
    
    
    @Test
    public void testDeleteCodeTypeSuccess() throws Exception{
    	CodeTypeDTO codeType = new CodeTypeDTO();
    	codeType.setCodeTypePk(UUID.randomUUID().toString());
    	codeType.setAppId("IConnect");
    	codeType.setCodeTypeId("ncs_cc2");
    	codeType.setCodeTypeDesc("test code type");
    	codeType.setCodeTypeTable("TBL_CODE_INT");
    	codeType.setColCodeTypeId("CODETYPE_ID");
    	codeType.setColCodeId("CODE_ID");
    	codeType.setColCodeDesc("CODE_DESC");
    	codeType.setColCodeSeq("CODE_SEQ");
    	codeType.setColLocale("LOCALE");
    	
    	codeType = this.codeAdminService.createCodeType(codeType);
    	
    	ResultActions result = mockMvc
        		.perform(delete("/api/codeAdmin/codeType/"+codeType.getCodeTypePk())
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
    	
    	result.andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void testCodesByCodeTypePkWithSuccess() throws Exception{
    	
    	ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codesByCodeTypePk?codeTypePk=acm_res&isExternal=false")
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
    	
    	result.andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void testCodesByCodeTypePkWithUnAuthorized() throws Exception{
    	ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codesByCodeTypePk?codeTypePk=country&isExternal=false")
        		.with(httpBasic("dumyuser","password1")));
    	result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    
    @Test
    public void testCodeAdminGetCodesByCodeTypePkWithAuthorized() throws Exception{
    	
    	ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codesByCodeTypePkAndCodeId?codeTypePk=process_st&codeId=F")
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
    	result.andDo(print()).andExpect(status().isOk())
    	.andExpect(jsonPath("$.[*].id").value(hasItem("process_st_F")));
    }
    
    @Test
    public void testCodeAdminGetCodesByCodeTypePkWithUnAuthorized() throws Exception{
    	
    	ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codesByCodeTypePkAndCodeId?codeTypePk=process_st&codeId=process_st")
        		.with(httpBasic("dumyuser","password1")));
    	
    	result.andDo(print()).andExpect(status().isUnauthorized());
    }
    
    
	@Test
    public void testGetCodeTypesByAppIdAndCodeTypePkNotWithAuthorized() throws Exception {

        ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codeTypesByAppIdAndCodeTypePkNot/?appId=IConnect&codeTypePk=status")
                .header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        result.andDo(print()).andExpect(status().isOk());
    }
	
	@Test
    public void testGetCodeTypesByAppIdAndCodeTypePkNotWithUnAuthorized() throws Exception {

        ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/codeTypesByAppIdAndCodeTypePkNot/?appId=IConnect&codeTypePk=status")
        		.with(httpBasic("dumyUser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	@Test
	public void testCodeAdminGetCodeWithAuthorized() throws Exception{
		ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/code/acm_res_ACTION")
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
        result.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.id").value("acm_res_ACTION"));
	}
	
	@Test
	public void testCodeAdminGetCodeWithUnAuthorized() throws Exception{
		ResultActions result = mockMvc
        		.perform(get("/api/codeAdmin/code/acm_res_ACTION")
        		.with(httpBasic("dumyUser","password1")));
		
        result.andDo(print()).andExpect(status().isUnauthorized());
	}


	@Test
	public void testCodeAdminCreateCodeWithAuthorized() throws Exception{
		CodeIntDTO codeInt = new CodeIntDTO();
		codeInt.setAppCode("IConnect");
		codeInt.setAppId("IConnect");
		codeInt.setId("task_st_E");
		codeInt.setCodeId("task_st");
		codeInt.setCodeTypeBZId("task_st");
		codeInt.setCodeTypePK("task_st");
		codeInt.setCodeId("E");
		codeInt.setCodeDesc("Complete Successful");
		codeInt.setCodeSeq(4);
		codeInt.setStatus("A");
		codeInt.setLocale("en");
		
		ResultActions result = mockMvc
        		.perform(post("/api/codeAdmin/code")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isCreated());
		
	}
	
	@Test
	public void testCodeAdminCreateCodeWithUnAuthorized() throws Exception{
		CodeIntDTO codeInt = new CodeIntDTO();
		codeInt.setAppCode("IConnect");
		codeInt.setAppId("IConnect");
		codeInt.setId("task_st_E");
		codeInt.setCodeId("task_st");
		codeInt.setCodeTypeBZId("task_st");
		codeInt.setCodeTypePK("task_st");
		codeInt.setCodeId("E");
		codeInt.setCodeDesc("Complete Successful");
		codeInt.setCodeSeq(4);
		codeInt.setStatus("A");
		codeInt.setLocale("en");
		
		ResultActions result = mockMvc
        		.perform(post("/api/codeAdmin/code")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testCodeAdminUpdateCodeWithAuthorized() throws Exception{
		CodeIntDTO codeInt = codeAdminService.findCodeById("process_st_A");
		codeInt.setStatus("I");
		codeInt.setUpdatedBy("configadmin");
		
		ResultActions result = mockMvc
        		.perform(put("/api/codeAdmin/code")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					//update back to previous status
					CodeIntDTO codeInt = codeAdminService.findCodeById("process_st_A");
					codeInt.setStatus("A");
					
					mockMvc.perform(put("/api/codeAdmin/code")
			        		.contentType(MediaType.APPLICATION_JSON)
			        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
                            .header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
				}
			});
	}
	
	@Test
	public void testCodeAdminUpdateCodeWithUnAuthorized() throws Exception{
		CodeIntDTO codeInt = codeAdminService.findCodeById("process_st_A");
		codeInt.setStatus("I");
		codeInt.setUpdatedBy("configadmin");
		
		ResultActions result = mockMvc
        		.perform(put("/api/codeAdmin/code")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	public void createProcessCodeInt() {
		CodeIntDTO codeInt = new CodeIntDTO();
		codeInt.setAppCode("IConnect");
		codeInt.setAppId("IConnect");
		codeInt.setId("process_st_A");
		codeInt.setCodeId("A");
		codeInt.setCodeDesc("Active");
		codeInt.setCodeSeq(1);
		codeInt.setStatus("A");
		codeInt.setLocale("en");
		codeInt.setCodeTypeBZId("process_st");
		codeInt.setCodeTypePK("process_st");
		try {
			codeInt.setExpiryDt(DateUtils.parseDate("2048-10-01", "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		codeAdminService.createCode(codeInt);
	}
	
	@Test
	@Transactional
	public void testCodeAdminDeleteCodeWithAuthorized() throws Exception{
		
		ResultActions result = mockMvc
        		.perform(delete("/api/codeAdmin/code?codeTypePk=process_st&codeId=A")
        		.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print())
			.andExpect(status().isOk())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					createProcessCodeInt();
				}
			});
	}
	
	@Test
	public void testCodeAdminDeleteCodeWithUnAuthorized() throws Exception{
		
		ResultActions result = mockMvc
        		.perform(delete("/api/codeAdmin/code?codeTypePk=process_st&codeId=A")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	public void testChangeCodeSeq() throws Exception{
		List<CodeIntDTO> codeInts = new ArrayList<CodeIntDTO>();
		
		CodeIntDTO codeInt = null;
		codeInt = codeAdminService.findCodeById("process_st_A");
		if(codeInt==null) {
			createProcessCodeInt();
			codeInt = codeAdminService.findCodeById("process_st_A");
		}else {
			codeInt.setCodeSeq(codeInt.getCodeSeq());
		}
		codeInts.add(codeInt);
		
		ResultActions result = mockMvc
        		.perform(put("/api/codeAdmin/changeCodeSeq")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInts))
				.header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateCodeWithCodeSequenceInvalid() throws Exception{
		CodeIntDTO codeInt = codeAdminService.findCodeById("process_st_A");
		codeInt.setCodeSeq(codeInt.getCodeSeq()+1);
		
		ResultActions result = mockMvc
        		.perform(put("/api/codeAdmin/code")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testUpdateCodeWithCodeSequenceInvalid2() throws Exception{
		CodeIntDTO codeInt = codeAdminService.findCodeById("process_st_A");
		codeInt.setExpiryDt(DateUtils.parseDate("2048-10-02", "yyyy-MM-dd"));
		codeInt.setEffectiveDt(DateUtils.parseDate("2048-11-02", "yyyy-MM-dd"));
		
		ResultActions result = mockMvc
        		.perform(put("/api/codeAdmin/code")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(codeInt))
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isBadRequest());
	}
}
