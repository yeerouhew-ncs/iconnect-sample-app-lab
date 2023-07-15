package com.ncs.iconnect.sample.lab.generated.approval.customerrequest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import javax.servlet.Filter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.approval.customerrequest.CustomerRequestDTO;
import com.ncs.iconnect.sample.lab.generated.approval.testdata.ApprovalRequestTestData;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iconnect.sample.lab.generated.web.rest.TestUtil;

import com.ncs.iconnect.sample.lab.generated.domain.enumeration.AccountStatus;

@SpringBootTest(classes = IconnectSampleAppLabApp.class)
public class CustomerRequestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AccountStatus DEFAULT_ACCOUNT_STATUS = AccountStatus.ACTIVE;
    private static final AccountStatus UPDATED_ACCOUNT_STATUS = AccountStatus.DEACTIVE;

    private static final String DEFAULT_EMAIL = "0@G.lD";
    private static final String UPDATED_EMAIL = "2g@AQ.k.sm";

    private static final String DEFAULT_TEL_MAIN = "AAAAAAAAAA";
    private static final String UPDATED_TEL_MAIN = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    TestTokenProvider testTokenProvider;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Filter springSecurityFilterChain;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
		objectMapper.findAndRegisterModules();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static CustomerRequestDTO createEntity() {
		CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
		
        customerRequestDTO.setName(DEFAULT_NAME);
        customerRequestDTO.setAccountStatus(DEFAULT_ACCOUNT_STATUS);
        customerRequestDTO.setEmail(DEFAULT_EMAIL);
        customerRequestDTO.setTelMain(DEFAULT_TEL_MAIN);
        customerRequestDTO.setDescription(DEFAULT_DESCRIPTION);
		
		customerRequestDTO.setApprovalRequest(ApprovalRequestTestData.newApprovalRequestDTO());
		return customerRequestDTO;
	}

    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }

	@Test
	@Transactional
	public void createCustomerRequest() throws Exception {
		// Create the Order
		CustomerRequestDTO customerRequestDTO = createEntity();

		ResultActions result = mockMvc
				.perform(post("/api/approval/customer-requests").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(customerRequestDTO)).header("Authorization",
								"Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		result.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("id").value(notNullValue()));
	}

	@Test
	@Transactional
	public void getCustomerRequest() throws Exception {
		CustomerRequestDTO customerRequestDTO = createEntity();

		ResultActions result = mockMvc
				.perform(post("/api/approval/customer-requests").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(customerRequestDTO)).header("Authorization",
								"Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		CustomerRequestDTO submittedCustomerRequestDTO = objectMapper
				.readValue(result.andReturn().getResponse().getContentAsString(), CustomerRequestDTO.class);

		mockMvc.perform(get("/api/approval/customer-requests/" + submittedCustomerRequestDTO.getId()).header("Authorization",
				"Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		result.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("id").value(notNullValue()));

	}

	@Test
	@Transactional
	public void updateCustomerRequest() throws Exception {
		CustomerRequestDTO customerRequestDTO = createEntity();

		ResultActions result = mockMvc
				.perform(post("/api/approval/customer-requests").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(customerRequestDTO)).header("Authorization",
								"Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		CustomerRequestDTO submittedCustomerRequestDTO = objectMapper
				.readValue(result.andReturn().getResponse().getContentAsString(), CustomerRequestDTO.class);

		ResultActions updatedResult = mockMvc.perform(put("/api/approval/customer-requests")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(submittedCustomerRequestDTO))
				.header("Authorization", "Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		updatedResult.andDo(print()).andExpect(status().is2xxSuccessful());
	}

	@Test
	@Transactional
	public void deleteCustomerRequest() throws Exception {
		CustomerRequestDTO customerRequestDTO = createEntity();

		ResultActions result = mockMvc
				.perform(post("/api/approval/customer-requests").contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(customerRequestDTO)).header("Authorization",
								"Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		CustomerRequestDTO submittedCustomerRequestDTO = objectMapper
				.readValue(result.andReturn().getResponse().getContentAsString(), CustomerRequestDTO.class);

		ResultActions deleteResult = mockMvc.perform(
				delete("/api/approval/customer-requests/" + submittedCustomerRequestDTO.getId()).header("Authorization",
						"Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));

		deleteResult.andDo(print()).andExpect(status().is2xxSuccessful());
	}

}
