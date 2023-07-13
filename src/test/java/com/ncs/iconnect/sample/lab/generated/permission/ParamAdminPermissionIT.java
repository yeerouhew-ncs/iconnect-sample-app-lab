package com.ncs.iconnect.sample.lab.generated.permission;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import com.ncs.iconnect.sample.lab.generated.web.rest.admin.param.ParamAdminResource;
import com.ncs.iforge5.param.service.ParamService;
import com.ncs.iforge5.param.to.ParamDTO;
import com.ncs.iforge5.param.to.ParamTO;
import com.ncs.itrust5.sss.dto.AppDTO;

import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class ParamAdminPermissionIT {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

	private MockMvc mockMvc;
    private MediaType mediaType = MediaType.APPLICATION_JSON;

    @Autowired
	private ParamService paramService;

    @Autowired
    private ParamAdminResource paramAdminResource;

	@Autowired
    private TestTokenProvider testTokenProvider;
	
	private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain).build();
	}

    @Test
	public void testGetAppListAuthorizedSuccess() throws Exception{
		
		ResultActions result = mockMvc.perform(get("/api/applicationList")
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
	    result.andDo(print()).andExpect(status().isOk());
	}
	
	
	@Test
	public void testGetAppListUnAuthorized() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/applicationList")
        		.contentType(MediaType.APPLICATION_JSON)
        		.with(httpBasic("dumyuser","password1")));
	    result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetParamsWithAuthorizedUserExpectSuccess() throws Exception {

		ParamDTO paramDto = new ParamDTO();
		paramDto.setAppId("abc");
		paramDto.setParamKey("def");
		paramDto.setParamValue("test_parameter");
		this.paramService.create(paramDto);
		
        // when
        ResultActions result = mockMvc.perform(get("/api/paramadmin/?appId=abc&paramKey=def&paramDesc=&effectiveDateAsStr=&expireDateAsStr=")
        		.contentType(MediaType.APPLICATION_JSON)
        		.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));

        // then
        result.andDo(print()).andExpect(status().isOk())
	        .andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult result) throws Exception {
						mockMvc.perform(delete("/api/paramadmin/abc/def")
								.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
					}
	        });
	}
	    
     @Test
     public void testGetParamsWithUnAuthorizedUserExpectUnSuccess() throws Exception {
        ResultActions result = mockMvc
        		.perform(get("/api/paramadmin/?appId=&paramKey=&paramDesc=&effectiveDateAsStr=&expireDateAsStr=")
        		.with(httpBasic("dumyuser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
     }
	
	
	@Test
	public void testCreateParameterWithAuthorizedExpectSuccess() throws Exception{
		ParamDTO paramDto = new ParamDTO();
		paramDto.setAppId("test_app");
		paramDto.setParamKey("test_app_testkey");
		paramDto.setParamValue("test_parameter");
		
		ResultActions result = mockMvc.perform(post("/api/paramadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(paramDto))
        		.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isCreated())
			.andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(delete("/api/paramadmin/test_app/test_app_testkey")
							.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
				}
			});
	}
	
	@Test
	public void testCreateParameterWithUnAuthorized() throws Exception{
		ParamDTO paramDto = new ParamDTO();
		paramDto.setAppId("test_app");
		paramDto.setParamKey("test_app_testkey");
		paramDto.setParamValue("test_parameter");
		
		ResultActions result = mockMvc.perform(post("/api/paramadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(paramDto))
        		.with(httpBasic("dumyUser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	
	@Test
	public void testUpdateParameterWithAuthorizedExpectSuccess() throws Exception{
		
		ParamDTO dto = new ParamDTO();
		dto.setAppId("IConnect");
		dto.setParamKey("UpdateParameter");
		this.paramService.create(dto);
		
		ResultActions result = mockMvc.perform(put("/api/paramadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(dto))
        		.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
		result.andDo(print()).andExpect(status().isOk())
		.andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult result) throws Exception {
				 mockMvc.perform(delete("/api/paramadmin/IConnect/UpdateParameter")
					.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
			}
		});
	}
	
	@Test
	public void testUpdateParameterWithUnAuthorizedExpectFailure() throws Exception{
		
		ParamDTO dto = new ParamDTO();
		dto.setParamDesc("updated_Desc");
		
		ResultActions result = mockMvc.perform(put("/api/paramadmin")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.convertObjectToJsonBytes(dto))
        		.with(httpBasic("dumyuser","password1")));
		
		result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
    
	@Test
    public void testGetParamKeysWithAuthorizedUserExpectSuccess() throws Exception {

		ParamDTO dto = new ParamDTO();
		dto.setAppId("IConnect");
		dto.setParamKey("findParameterKey");
		this.paramService.create(dto);
		
        // when
        ResultActions result = mockMvc.perform(get("/api/paramadmin/IConnect/findParameterKey")
        		.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		
        final String paramKey = getReturnJSONValue(result.andReturn().getResponse().getContentAsString(),"paramKey");
        
        // then
        result.andDo(print()).andExpect(status().isOk())
	        .andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult result) throws Exception {
					mockMvc.perform(delete("/api/paramadmin/IConnect/"+paramKey)
        		           .header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
				}
	        });
    }
	
	@Test
    public void testGetParamKeysWithUnAuthorized() throws Exception {
		
        ResultActions result = mockMvc.perform(get("/api/paramadmin/IConnect/findParameterKey2")
        		.with(httpBasic("dumyuser","password1")));
        
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	
	@Test
	public void testDeleteParametersWithAuthorizedUserExpectSuccess() throws Exception {
		List<ParamTO> findAppList = new ArrayList<ParamTO>();
		
		 ResultActions result = mockMvc.perform(delete("/api/paramadmin")
				 	.contentType(MediaType.APPLICATION_JSON)
	        		.content(TestUtil.convertObjectToJsonBytes(findAppList))
	        		.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
		 result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteParametersWithUnAuthorizedUser() throws Exception {
		List<AppDTO> findAppList = this.paramService.getAppList();
		assertThat(findAppList).isNotEmpty();
		
		 ResultActions result = mockMvc.perform(delete("/api/paramadmin")
				 	.contentType(MediaType.APPLICATION_JSON)
	        		.content(TestUtil.convertObjectToJsonBytes(findAppList))
	        		.with(httpBasic("dumyUser","password1")));
		 
		 result.andDo(print()).andExpect(status().isUnauthorized());
	}
	
	
	private ParamDTO createParamDTO(String appId,String paramKey) {
		ParamDTO dto = new ParamDTO();
		dto.setAppId(appId);
		dto.setParamKey(paramKey);
		dto.setParamDesc(paramKey);
		return dto;
	}
	
	@Test
    public void testDeleteParameterWithAuthorizedUserExpectSuccess() throws Exception{
	
		ParamDTO dto = createParamDTO("IConnect","testkey1");
		this.paramService.create(dto);
		
    	ResultActions result = mockMvc
        		.perform(delete("/api/paramadmin/IConnect/testkey1")
				.header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
    	
        result.andDo(print()).andExpect(status().isOk());
    }
	
	@Test
    public void testDeleteParameterWithUnAuthorizedUserExpectUnSuccess() throws Exception{
    	ResultActions result = mockMvc
        		.perform(delete("/api/paramadmin/IConnect/testkey1")
        		.with(httpBasic("dumyuser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	    
	@Test
	public void testGetAllParamKeysWithUnAuthorized() throws Exception {

        ResultActions result = mockMvc.perform(get("/api/paramadmin/paramkeys/?appId=IConnect")
                .header("Authorization","Bearer " + obtainAccessToken("configadmin", "DEF-user-configadmin", "password1")));
        result.andDo(print()).andExpect(status().isOk());
    }
	
	@Test
    public void testGetAllParamKeysWithUnAuthorizedUserExpectUnSuccess() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/paramadmin/paramkeys/?appId=")
        		.with(httpBasic("dumyuser","password1")));
        result.andDo(print()).andExpect(status().isUnauthorized());
    }
	
	public static String getReturnJSONValue(String jsonReturn,String parameter) throws Exception{
	    	JSONObject json = new JSONObject(jsonReturn);
	    	return (String) json.get(parameter);
	}
}
